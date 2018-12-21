package Core;

import UserList.Message;
import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import UserList.User;
/*
Собственно, это и есть мотор клиентской части чата.
Являясь слушателем для TCPConnection, он обрабатывает события от соединения и генерирует события для WorkerListener,
которым является главный Контроллер GUI(CController).
 */
public class Worker implements TCPConnectionListener {

    private TCPConnection tcpConnection;
    private final WorkerListener workerListener;
    private User user;


    public Worker(WorkerListener workerListener, String IP, int PORT, User user) {
        this.workerListener = workerListener;
        tcpConnection = new TCPConnection(IP, PORT, Worker.this);
        if (tcpConnection.getSocket() != null)
            tcpConnection.sendMessage(new Message(user));
    }


    public synchronized void stopConnection() {
        tcpConnection.closeConnection();
    }

    public boolean sendTextMsg(User user, String msg) {
        if (tcpConnection.getSocket() != null) {
            tcpConnection.sendMessage(new Message(msg));
            return true;
        }else
            return false;
    }


    @Override
    public void onConnection(TCPConnection tcpConnection) {
        workerListener.onConnection();
    }

    @Override
    public void onDisconnection(TCPConnection tcpConnection) {
        workerListener.onDisconnection();
    }

    @Override
    public void onRecieveMessage(TCPConnection tcpConnection, Message msg) {
        switch (msg.getType()) {
            case isAuth:
            case isTextMsg:
                workerListener.gotTextMsg(msg.getUser().getLogin() + ": " + msg.getTextMsg());
                break;
        }

    }

    @Override
    public synchronized boolean onAuthorization(TCPConnection tcpConnection, Message msg) {
        boolean answer = msg.isSignIn();
        workerListener.signIn(answer);
        return answer;
    }

    @Override
    public void connectionException(Exception e) {
        workerListener.connectionException(e);
    }

    @Override
    public void connectionException(TCPConnection tcpConnection, Exception e) {
        workerListener.connectionException(e);
    }

    @Override
    public void recieveMessageException(TCPConnection tcpConnection, Exception e) {
        workerListener.recieveMessageException(e);
    }
}
