package Core;

import Statistic.IStatisticServer;
import Statistic.StatisticCollector;
import UserList.Message;
import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import UserList.MsgType;
import UserList.User;
import DB.DBWorker;
import DB.MySQLWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
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
    private DBWorker dbWorker;
    private IStatisticServer statisticServer;


    public ServerWorker(int port, long maxTimeUnauthenticated, long conInspectionTime, int maxCountFailedAuthor, ServerWorkerListener serverWorkerListener) {
        unAuthenticatedConnections = new ArrayList();
        authenticatedConnections = new HashMap();
        this.port = port;
        this.serverWorkerListener = serverWorkerListener;
        connectionInspector = new ConnectionsInspector(this, maxCountFailedAuthor, conInspectionTime, maxTimeUnauthenticated );
        isRun = true;
        new Thread (()->{
            try {
                dbWorker = new MySQLWorker(); //
            } catch (SQLException e) {
                System.out.println("Не удалось соединиться с базой данных: " + e.getMessage());
            }
        }).start();
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

        new Thread(() -> statisticServer = StatisticCollector.getInstance()).start();
    }

    public synchronized ArrayList<TCPConnection> getUnAuthenticatedConnections() {
        return unAuthenticatedConnections;
    }

    @Override
    public synchronized void onConnection(TCPConnection tcpConnection) {
        getUnAuthenticatedConnections().add(tcpConnection);
        statisticServer.increaseUnauthorized();
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
        String login = msg.getUser().getLogin();
        String password = msg.getUser().getPassword();
        boolean authenticated;

        try {
            if (dbWorker.userValidated(login,password)){
                getUnAuthenticatedConnections().remove(tcpConnection);
                checkForDublicateConnection(login);
                authenticatedConnections.put(login, tcpConnection);
                tcpConnection.setLogin(login);
                tcpConnection.setAuthenticationAttempts(0);
                authenticated = true;
                statisticServer.increaseAuthorized();
            }else {
                int countFailedAuthor = tcpConnection.getAuthenticationAttempts();
                tcpConnection.setAuthenticationAttempts(++countFailedAuthor);
                connectionInspector.connectionDestructor(tcpConnection, countFailedAuthor);
                authenticated = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            authenticated = false;
        }
        authAnswer(tcpConnection, authenticated);
        return authenticated;
    }

    private synchronized void checkForDublicateConnection(String loginInBase) {
        TCPConnection connection = authenticatedConnections.get(loginInBase);
        if (connection != null) {
            connection.closeConnection();
        }
    }

    private void authAnswer(TCPConnection tcpConnection, boolean authenticated) {
       //Нужно допилить полное формирование юзера для его отправки клиенту
        User user = new User(tcpConnection.getLogin());
        Message msg = new Message(user, MsgType.authentication);
        String textAnswer;
        if (authenticated)
            textAnswer = "Вы авторизованы!";
        else
            textAnswer = "Неверный логин/пароль";
        msg.authenticated(authenticated);
        msg.setTextMsg(textAnswer);

        tcpConnection.sendMessage(msg);
    }

    @Override
    public synchronized void connectionException(Exception e) {
        System.out.println(e.getMessage());

    }

    @Override
    public synchronized void connectionException(TCPConnection tcpConnection, Exception e) {
        System.out.println(tcpConnection + ": " + e.getMessage());

    }

    @Override
    public synchronized void recieveMessageException(TCPConnection tcpConnection, Exception e) {
        System.out.println(tcpConnection + ": " + e.getMessage());

    }

    @Override
    public boolean onRegistration(TCPConnection tcpConnection, Message msg) {

        Message answerMsg = new Message(msg.getUser(), MsgType.authentication);
        User user = msg.getUser();
        try {
            if (dbWorker.putUser(user)) {
                tcpConnection.setLogin(user.getLogin());
                authenticatedConnections.put(tcpConnection.getLogin(), tcpConnection);
                answerMsg.authenticated(true);
                answerMsg.setTextMsg("Добро пожаловать, " + user.getLastName() + " " + user.getFirstName() + " :)");
            }else{
                answerMsg.authenticated(false);
                answerMsg.setTextMsg("Такой логин уже существует");
            }
        } catch (SQLException e) {
            answerMsg.authenticated(false);
            answerMsg.setTextMsg("Ошибка обработки запроса сервером");
            e.printStackTrace();
        }
        tcpConnection.sendMessage(answerMsg);
        return answerMsg.authenticated();
    }
}
