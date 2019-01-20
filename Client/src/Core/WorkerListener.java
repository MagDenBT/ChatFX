package Core;

import UserList.Message;

public interface WorkerListener {

    void gotTextMsg(String msg);

    void connectionException(Exception e);

    void recieveMessageException(Exception e);

    void onConnection();

    void onDisconnection();

    void onSigned(Message msg);
}
