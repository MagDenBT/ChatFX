package Connector;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {

    private Socket socket;
    private final TCPConnectionListener listener;
    private ObjectInputStream in ;
    private ObjectOutputStream out;
    private StreamReader streamReader;
    private Thread rxThread;

    public TCPConnection(String IP, int port, TCPConnectionListener listener) {
        this.listener = listener;
        try {
            this.socket = new Socket(IP, port);
        } catch (IOException e) {
            listener.connectionException(this,e);
        }
        initialization();

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



    protected synchronized void closeConnection() {
        try {
            String antl;
            streamReader.stop();
            in.close();
            out.close();
            socket.close();
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


    @Override
    public String toString() {
        return "IP" + socket.getInetAddress() + ":" + socket.getPort();
    }
}
