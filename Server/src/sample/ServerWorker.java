package sample;

import New.Connector.Message;
import New.Connector.TCPConnection;
import New.Connector.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerWorker implements TCPConnectionListener {

    private int port;
    private ServerSocket serverSocket;
    private HashMap<String,TCPConnection> authorizedConnections,
    private volatile ArrayList<TCPConnection>  unAuthorizedConnections
    private ServerWorkerListener serverWorkerListener;
    private volatile boolean isRun;
    private final long maxTimeUnAuthorized;

    public ServerWorker(int port, long maxTimeUnAuthorized, ServerWorkerListener serverWorkerListener) {
        unAuthorizedConnections = new ArrayList();
        authorizedConnections = new HashMap();
        this.port = port;
        this.maxTimeUnAuthorized = maxTimeUnAuthorized;
        this.serverWorkerListener = serverWorkerListener;
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

    }

    @Override
    public synchronized void onRecieveMessage(TCPConnection tcpConnection, Message msg) {

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
            tcpConnection.setCountOfTryAuthor(0);
            return true;
        }else{
            long timeUnAuthorized = System.currentTimeMillis() - tcpConnection.getTimeOfStartConnection();
            if(timeUnAuthorized < )
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
