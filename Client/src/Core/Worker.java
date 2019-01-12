package Core;

import UserList.Message;
import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import UserList.MsgType;
import UserList.User;

/**
 * Собственно, это и есть мотор клиентской части чата.
 * Являясь слушателем для TCPConnection, он обрабатывает события от соединения и генерирует события для WorkerListener,
 * которым является главный Контроллер GUI(CController).
 */
public class Worker implements TCPConnectionListener {

    private TCPConnection tcpConnection;
    private final WorkerListener workerListener;
    private String host;
    private int port;


    public Worker(WorkerListener workerListener, String host, int port) {
        this.workerListener = workerListener;
        this.host = host;
        this.port = port;
        startConnection();
    }


    private synchronized void startConnection() {
        tcpConnection = new TCPConnection(host, port, Worker.this);
    }


    public synchronized void restartConnection() {
        tcpConnection.closeConnection();
        startConnection();
    }
/*
//////Блок кода передающий сообщения на сервер
 */
    /**
     * Передает сообщение в поток, если сокет закрыт - возвращает Ложь
     * @param msg
     * @return
     */
    private boolean sendMsg(Message msg) {
        if (tcpConnection.getSocket() != null) {
            tcpConnection.sendMessage(msg);
            return true;
        }else
            return false;
    }

    public boolean sendTextMsg(String text) {
        return sendMsg(new Message(text));
    }

    public boolean AuthOnServer(User user) {
        return sendMsg(new Message(user,MsgType.isAuth));
    }
    public boolean updateUserAtServer(User user) {
        return sendMsg(new Message(user,MsgType.isUserUpdate));
    }
  /*
Блок кода передающий сообщения на сервер //////
 */



    @Override
    public void onConnection(TCPConnection tcpConnection) {
        workerListener.onConnection();
    }

    @Override
    public void onDisconnection(TCPConnection tcpConnection) {
        workerListener.onDisconnection();
    }

    /**
     * Ловит пользовательское сообщение от StreamReader
     * и передает его слушателю Воркера
     * @param tcpConnection
     * @param msg
     */
    @Override
    public void onRecieveMessage(TCPConnection tcpConnection, Message msg) {
        switch (msg.getType()) {
            case isAuth:
            case isTextMsg:
                workerListener.gotTextMsg(msg.getUser().getLogin() + ": " + msg.getTextMsg());
                break;
        }

    }

    /**
     * Вызывается StreamReader'ом, сообщает слушателю Воркера пройдена ли авторизация на Сервер или нет
     * Также возвращает ответ сервера вызывателю(вызыватель присваевает этот ответ в свойство TCPConnection)
     * дабы в дальнейшем уже по свойству TCPConnection можно было определить авторизованность соединения
     * @param tcpConnection
     * @param msg
     * @return
     */
    @Override
    public synchronized boolean onAuthorization(TCPConnection tcpConnection, Message msg) {
       if (msg.isSignIn()) workerListener.onSigned();
        return msg.isSignIn();
    }

    /**
     * Ловит ошибку создания/закрытия соедиения
     * @param e
     */
    @Override
    public void connectionException(Exception e) {
        workerListener.connectionException(e);
    }

    /**
     * Ловит ошибку создания/закрытия соедиения
     * @param e
     */
    @Override
    public void connectionException(TCPConnection tcpConnection, Exception e) {
        workerListener.connectionException(e);
    }

    /**
     * Ловит ошибку чтения сообщения из потока в соединении
     * @param e
     */
    @Override
    public void recieveMessageException(TCPConnection tcpConnection, Exception e) {
        workerListener.recieveMessageException(e);
    }
}
