package sample;

public interface WorkerListener {

    void gotTextMsg(String msg);

    void connectionException(Exception e);

    void recieveMessageException(Exception e);

    void onConnection();

    void onDisconnection();

    void signIn(boolean answer);
}
