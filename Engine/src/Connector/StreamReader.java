package Connector;

import java.io.BufferedReader;
import java.io.IOException;


class StreamReader {
    private final BufferedReader in;
    private final TCPConnection tcpConnection;
    private Thread thread;
    private final TCPConnectionListener listener;

    protected StreamReader(BufferedReader in, TCPConnection tcpConnection, TCPConnectionListener listener) {
        this.tcpConnection = tcpConnection;
        this.in = in;
        this.listener = listener;
        thread = new Thread(() -> {
            String msg = null;
            while (!thread.isInterrupted()) {
                try {
                    msg = in.readLine();
                } catch (IOException e) {
                    listener.recieveMessageException(tcpConnection, e);
                    tcpConnection.closeConnection();
                }finally {
                    if (msg != null)
                        listener.onRecieveTextMessage(tcpConnection, msg);
                    else break;
                }
            }

            return;
        });
        thread.start();
    }

    protected void stop(){
        thread.interrupt();
    }


}
