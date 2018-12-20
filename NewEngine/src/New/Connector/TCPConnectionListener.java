package New.Connector;

import New.UserList.Message;

public interface TCPConnectionListener {

    void onConnection(TCPConnection tcpConnection);

    void onDisconnection(TCPConnection tcpConnection);

    void onRecieveMessage(TCPConnection tcpConnection, Message msg);

    boolean onAuthorization(TCPConnection tcpConnection, Message msg);

    void connectionException(Exception e);

    void connectionException(TCPConnection tcpConnection,Exception e);

    void recieveMessageException(TCPConnection tcpConnection, Exception e);


}
