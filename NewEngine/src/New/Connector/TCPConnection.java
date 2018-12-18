package New.Connector;

import java.io.*;
import java.net.Socket;

public class TCPConnection {

    private Socket socket;
    private final TCPConnectionListener listener;
    private ObjectInputStream in ;
    private ObjectOutputStream out;
    private StreamReader streamReader;
    protected volatile boolean isAuthorizated = false;
    private volatile long timeOfStartConnection;
    protected volatile int countOfTryAuthor;

    public TCPConnection(String IP, int port, TCPConnectionListener listener) {
        this.listener = listener;
        try {
            this.socket = new Socket(IP, port);
            timeOfStartConnection = System.currentTimeMillis();
            initialization();
        } catch (IOException e) {
            listener.connectionException(this,e);
        }


    }

    public TCPConnection(Socket socket, TCPConnectionListener listener) {
        this.listener = listener;
        this.socket = socket;
        initialization();
    }


    private void initialization() {
        try {
            in = new ObjectInputStream (socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
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
            String test;
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


    public int getCountOfTryAuthor() {
        return countOfTryAuthor;
    }

    public void setCountOfTryAuthor(int countOfTryAuthor) {
        this.countOfTryAuthor = countOfTryAuthor;
    }

    @Override
    public String toString() {
        return "IP" + socket.getInetAddress() + ":" + socket.getPort();
    }
}