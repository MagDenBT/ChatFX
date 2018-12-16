package Connector;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {

    private Socket socket;
    private final TCPConnectionListener listener;
    private BufferedReader in ;
    private BufferedWriter out;
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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
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
            streamReader.stop();
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            listener.connectionException(this, e);
        }
    }

    public boolean sendMessage(String msg) {

        try {
            out.write(msg + "\n");
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
