package sample;

import New.Connector.Message;
import New.Connector.MsgType;
import New.Connector.TCPConnection;
import New.Connector.TCPConnectionListener;
import New.UserList.User;

public class Worker implements TCPConnectionListener {

    private TCPConnection tcpConnection;
    private final WorkerListener workerListener;


    public Worker(WorkerListener workerListener, String IP, int PORT, String login, String password) {
        this.workerListener = workerListener;
        startConnection(IP, PORT, login, password);
    }


    public void startConnection(String IP, int PORT, String login, String password) {
        tcpConnection = new TCPConnection(IP, PORT, Worker.this);
        if (tcpConnection.getSocket() != null)
            tcpConnection.sendMessage(new Message(MsgType.isAuth, new User(login, password), null));
    }

    public void stopConnection() {
        tcpConnection.closeConnection();
    }

    public boolean sendTextMsg(User user, String msg) {
        if (tcpConnection.getSocket() != null) {
            tcpConnection.sendMessage(new Message(MsgType.isTextMsg, user, msg));
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
