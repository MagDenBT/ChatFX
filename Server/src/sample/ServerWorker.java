package sample;

import New.Connector.Message;
import New.Connector.TCPConnection;
import New.Connector.TCPConnectionListener;
import New.UserList.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerWorker implements TCPConnectionListener {

    private int port;
    private ServerSocket serverSocket;
    private HashMap<String, TCPConnection> authorizedConnections;
    private volatile ArrayList<TCPConnection> unAuthorizedConnections;
    private ServerWorkerListener serverWorkerListener;
    private volatile boolean isRun;
    private final ConnectionsInspector connectionInspector;



    public ServerWorker(int port, long maxTimeUnAuthorized, long conInspectionTime, int maxCountFailedAuthor, ServerWorkerListener serverWorkerListener) {
        unAuthorizedConnections = new ArrayList();
        authorizedConnections = new HashMap();
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

    public synchronized ArrayList<TCPConnection> getUnAuthorizedConnections() {
        return unAuthorizedConnections;
    }

    @Override
    public synchronized void onConnection(TCPConnection tcpConnection) {
        getUnAuthorizedConnections().add(tcpConnection);
    }

    @Override
    public synchronized void onDisconnection(TCPConnection tcpConnection) {
        if (authorizedConnections.containsValue(tcpConnection)){
            Collection<TCPConnection>  col =authorizedConnections.values();
            col.remove(tcpConnection);
        }
        else getUnAuthorizedConnections().remove(tcpConnection);

    }

    @Override
    public synchronized void onRecieveMessage(TCPConnection tcpConnection, Message msg) {
        msg.setUser(new User(tcpConnection.getLogin()));
        for (Map.Entry<String, TCPConnection> con :authorizedConnections.entrySet()) {
            if(!con.equals(tcpConnection))
                con.getValue().sendMessage(msg);
        }
    }

    @Override
    public synchronized boolean onAuthorization(TCPConnection tcpConnection, Message msg) {
        String loginInBase = "testLogin";
        String passwordInBase = "testPassword";
        String firstLastNameInBase = "ИмяФамилияТест";
        String login = msg.getUser().getLogin();
        String password = msg.getUser().getPassword();

        if(login.equals(loginInBase) && password.equals(passwordInBase)){
            getUnAuthorizedConnections().remove(tcpConnection);
            authorizedConnections.put(loginInBase, tcpConnection);
            tcpConnection.setLogin(loginInBase);
            tcpConnection.setCountOfTryAuthor(0);
            return true;
        }else {
            int countFailedAuthor = tcpConnection.getCountOfTryAuthor();
            tcpConnection.setCountOfTryAuthor(++countFailedAuthor);
            connectionInspector.conncetionDestructor(tcpConnection, countFailedAuthor);
            return false;
        }
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
