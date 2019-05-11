package Connector;

import UserList.Message;

import java.io.*;
import java.net.Socket;


public class TCPConnection {

    private Socket socket;
    private final TCPConnectionListener listener;
    private ObjectInputStream in ;
    private ObjectOutputStream out;
    private StreamReader streamReader;
    protected volatile boolean authenticated = false;
    private volatile long timeOfStartConnection;
    protected volatile int authenticationAttempts;
    private volatile String login;

    /**
     * Конструктор для клиента, в котором "тяжелая" часть инициализации происходит в отдельном потоке,
     * чтобы UI не ждал потднятия соединения
     * @param IP
     * @param port
     * @param listener
     */
    public TCPConnection(String IP, int port, TCPConnectionListener listener) {
        this.listener = listener;
       new Thread(()-> {
           try {
               this.socket = new Socket(IP, port);
               timeOfStartConnection = System.currentTimeMillis();
               initialization();
           } catch (IOException e) {
               listener.connectionException(this, e);
           }
       }).start();

    }

    public TCPConnection(Socket socket, TCPConnectionListener listener) {
        this.listener = listener;
        this.socket = socket;
        initialization();
    }


    private void initialization() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream (socket.getInputStream());
            listener.onConnection(this);
        } catch (IOException e) {
            listener.connectionException(this, e);
            closeConnection();
            return;
        }

        streamReader = new StreamReader(in, this, listener);
    }



    public synchronized void closeConnection() {
        try {
            streamReader.stop();
            in.close();
            out.close();
            socket.close();
            listener.onDisconnection(this);
        } catch (IOException e) {
            listener.connectionException(this, e);
        }
    }

    public boolean sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public long getTimeOfStartConnection() {
        return timeOfStartConnection;
    }


    public int getAuthenticationAttempts() {
        return authenticationAttempts;
    }

    public void setAuthenticationAttempts(int count) {
        this.authenticationAttempts = count;
    }

    public synchronized String getLogin() {
        return login;
    }

    public synchronized void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "IP" + socket!=null? socket.getInetAddress() + ":" + socket.getPort():"...";
    }
}
