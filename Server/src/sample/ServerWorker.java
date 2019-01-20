package sample;

import UserList.Message;
import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import UserList.MsgType;
import UserList.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerWorker implements TCPConnectionListener {

    private int port;
    private ServerSocket serverSocket;
    private HashMap<String, TCPConnection> authenticatedConnections;
    private volatile ArrayList<TCPConnection> unAuthenticatedConnections;
    private ServerWorkerListener serverWorkerListener;
    private volatile boolean isRun;
    private final ConnectionsInspector connectionInspector;



    public ServerWorker(int port, long maxTimeUnAuthorized, long conInspectionTime, int maxCountFailedAuthor, ServerWorkerListener serverWorkerListener) {
        unAuthenticatedConnections = new ArrayList();
        authenticatedConnections = new HashMap();
        this.port = port;
        this.serverWorkerListener = serverWorkerListener;
        connectionInspector = new ConnectionsInspector(this, maxCountFailedAuthor, conInspectionTime, maxTimeUnAuthorized );
        isRun = true;
        Thread rxThread = new Thread(() -> {
            while (true) {
                if(isRun) {
                    try {
                        new TCPConnection(serverSocket.accept(), ServerWorker.this);
                    } catch (IOException e) {
                        serverWorkerListener.tcpConnectionCreateException(e);
                    }
                }
            }
        });

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            isRun = false;
            serverWorkerListener.serverSocketCreateException(e);
        }
        rxThread.start();
    }

    public synchronized ArrayList<TCPConnection> getUnAuthenticatedConnections() {
        return unAuthenticatedConnections;
    }

    @Override
    public synchronized void onConnection(TCPConnection tcpConnection) {
        getUnAuthenticatedConnections().add(tcpConnection);
    }

    @Override
    public synchronized void onDisconnection(TCPConnection tcpConnection) {
        if (authenticatedConnections.containsValue(tcpConnection)){
            Collection<TCPConnection>  col = authenticatedConnections.values();
            col.remove(tcpConnection);
        }
        else getUnAuthenticatedConnections().remove(tcpConnection);

    }

    @Override
    public synchronized void onRecieveMessage(TCPConnection tcpConnection, Message msg) {
        msg.setUser(new User(tcpConnection.getLogin()));
        for (Map.Entry<String, TCPConnection> entry : authenticatedConnections.entrySet()) {
            TCPConnection con = entry.getValue();
           // if(!con.equals(tcpConnection))
                con.sendMessage(msg);
        }
    }

    @Override
    public synchronized boolean onAuthentication(TCPConnection tcpConnection, Message msg) {
        String loginInBase = "testLogin";
        String passwordInBase = "testPassword";
        String firstLastNameInBase = "ИмяФамилияТест";
        String login = msg.getUser().getLogin();
        String password = msg.getUser().getPassword();
        boolean authenticated;
        if(login.equals(loginInBase) && password.equals(passwordInBase)){
            getUnAuthenticatedConnections().remove(tcpConnection);
            checkOfTallyConnection(loginInBase);
            authenticatedConnections.put(loginInBase, tcpConnection);
            tcpConnection.setLogin(loginInBase);
            tcpConnection.setAuthenticationAttempts(0);
            authenticated = true;
        }else {
            int countFailedAuthor = tcpConnection.getAuthenticationAttempts();
            tcpConnection.setAuthenticationAttempts(++countFailedAuthor);
            connectionInspector.connectionDestructor(tcpConnection, countFailedAuthor);
            authenticated = false;
        }
        authAnswer(tcpConnection, authenticated);
        return authenticated;
    }

    private synchronized void checkOfTallyConnection(String loginInBase) {
        TCPConnection connection = authenticatedConnections.get(loginInBase);
        if (connection != null) {
            connection.closeConnection();
        }
    }

    private void authAnswer(TCPConnection tcpConnection, boolean authenticated) {
        User user = new User(tcpConnection.getLogin());
        Message msg = new Message(user, MsgType.authentication);
        msg.authenticated(authenticated);
        //Здесь надо реализвать запаковку в Message причины фейла авторизации, если она есть
        tcpConnection.sendMessage(msg);
    }

    @Override
    public synchronized void connectionException(Exception e) {

    }

    @Override
    public synchronized void connectionException(TCPConnection tcpConnection, Exception e) {

    }

    @Override
    public synchronized void recieveMessageException(TCPConnection tcpConnection, Exception e) {

    }
}
