package Connector;


import UserList.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

/*
Класс, отвечающие за чтение и обработку сообщений из входящего потока.
Работает в отдельном потоке
 */
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
                      break; //на всякий случай.Если цикл не будет прибит, прервем его здесь. Карочь, по-любому хана
                }finally {
                    if (msg != null) {
                        switch (msg.getType()) {
                            case isTextMsg:
                                listener.onRecieveMessage(tcpConnection, msg);
                                break;
                            case isAuth:
                                tcpConnection.isAuthorizated = listener.onAuthorization(tcpConnection, msg);

                        }
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
