package New.Connector;

public interface TCPConnectionListener {

    void onConnection(TCPConnection tcpConnection);

    void onDisconnection(TCPConnection tcpConnection);

    void onRecieveTextMessage(TCPConnection tcpConnection, String msg);

    void connectionException(Exception e);

    void connectionException(TCPConnection tcpConnection,Exception e);

    void recieveMessageException(TCPConnection tcpConnection, Exception e);


}
