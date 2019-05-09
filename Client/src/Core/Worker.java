package Core;

import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import UserList.Message;
import UserList.MsgType;
import UserList.User;

import java.util.ArrayList;

/**
 * Собственно, это и есть мотор клиентской части чата.
 * Являясь слушателем для TCPConnection, он обрабатывает события от соединения и генерирует события для WorkerListener,
 * которым является главный Контроллер GUI(CController).
 */
public class Worker implements TCPConnectionListener {

    private TCPConnection tcpConnection;
    private ArrayList<WorkerListener> workerListeners;
    private String host;
    private int port;
    private static volatile Worker instance;


    public static Worker getInstance() {
        Worker localInstance = instance;
        if (localInstance == null) {
            synchronized (Worker.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Worker();
                }
            }
        }
        return localInstance;
    }

    private Worker() {
        workerListeners = new ArrayList();
    }

    public void addListener(WorkerListener listener) {
        workerListeners.add(listener);
    }

    public void removeListener(WorkerListener listener) {
        workerListeners.remove(listener);
    }

    public void setWorker(String host, int port) {
        this.host = host;
        this.port = port;
        if (tcpConnection != null)
            startConnection();
        else restartConnection();
    }
//    public Worker(WorkerListener workerListener, String host, int port) {
//        this.workerListener = workerListener;
//        this.host = host;
//        this.port = port;
//        startConnection();
//    }


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
     *
     * @param msg
     * @return
     */
    private boolean sendMsg(Message msg) {
        if (tcpConnection.getSocket() != null) {
            tcpConnection.sendMessage(msg);
            return true;
        } else
            return false;
    }

    public boolean sendTextMsg(String text) {
        return sendMsg(new Message(text));
    }

    public boolean sendAuthentication(User user) {
        return sendMsg(new Message(user, MsgType.authentication));
    }

    public boolean updateUserAtServer(User user) {
        return sendMsg(new Message(user, MsgType.userUpdate));
    }
  /*
Блок кода передающий сообщения на сервер //////
 */


    @Override
    public void onConnection(TCPConnection tcpConnection) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.onConnection();
        }

    }

    @Override
    public void onDisconnection(TCPConnection tcpConnection) {

    }

    /**
     * Ловит пользовательское сообщение от StreamReader
     * и передает его слушателю Воркера
     *
     * @param tcpConnection
     * @param msg
     */
    @Override
    public void onRecieveMessage(TCPConnection tcpConnection, Message msg) {
        switch (msg.getType()) {
            case authentication:
            case textMsg:
                onRecieveMessageToAll(msg.getUser().getLogin() + ": " + msg.getTextMsg());
                break;
        }

    }

    private void onRecieveMessageToAll(String text) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.gotTextMsg(text);
        }
    }

    /**
     * Вызывается StreamReader'ом, сообщает слушателю Воркера пройдена ли авторизация на Сервер или нет
     * Также возвращает ответ сервера вызывателю(вызыватель присваевает этот ответ в свойство TCPConnection)
     * дабы в дальнейшем уже по свойству TCPConnection можно было определить авторизованность соединения
     *
     * @param tcpConnection
     * @param msg
     * @return
     */
    @Override
    public synchronized boolean onAuthentication(TCPConnection tcpConnection, Message msg) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.onSigned(msg);
        }
        return msg.authenticated();
    }

    @Override
    public boolean onRegistration(TCPConnection tcpConnection, Message msg) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.onRegistration(msg);
        }
        return msg.authenticated();
    }

    /**
     * Ловит ошибку создания/закрытия соедиения
     *
     * @param e
     */
    @Override
    public void connectionException(Exception e) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.connectionException(e);
        }
    }

    /**
     * Ловит ошибку создания/закрытия соедиения
     *
     * @param e
     */
    @Override
    public void connectionException(TCPConnection tcpConnection, Exception e) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.connectionException(e);
        }
    }

    /**
     * Ловит ошибку чтения сообщения из потока в соединении
     *
     * @param e
     */
    @Override
    public void recieveMessageException(TCPConnection tcpConnection, Exception e) {
        for (WorkerListener workerListener : workerListeners
                ) {
            workerListener.recieveMessageException(e);
        }
    }
}
