package Connector;


import java.io.IOException;
import java.io.ObjectInputStream;


class StreamReader {
    private final ObjectInputStream in;
    private final TCPConnection tcpConnection;
    private Thread thread;
    private final TCPConnectionListener listener;

    protected StreamReader(ObjectInputStream in, TCPConnection tcpConnection, TCPConnectionListener listener) {
        this.tcpConnection = tcpConnection;
        this.in = in;
        this.listener = listener;
        thread = new Thread(() -> {
            Message msg = null;
            while (!thread.isInterrupted()) {
                try {
                    msg = (Message) in.readObject();
                } catch (ClassNotFoundException e) {
                    //   тут подумать надо, как обработать ошибку
                } catch (IOException e) {
                      listener.recieveMessageException(tcpConnection, e);
                      tcpConnection.closeConnection();
                }finally {
                    if (msg != null) {
                        listener.onRecieveMessage(tcpConnection, msg);
                    }
                }
                }
        });
        thread.start();
    }

    protected void stop(){
        thread.interrupt();
    }


}
