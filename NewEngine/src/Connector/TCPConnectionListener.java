package Connector;

import UserList.Message;

public interface TCPConnectionListener {

    void onConnection(TCPConnection tcpConnection);

    void onDisconnection(TCPConnection tcpConnection);

    void onRecieveMessage(TCPConnection tcpConnection, Message msg);

    boolean onAuthentication(TCPConnection tcpConnection, Message msg);

    void connectionException(Exception e);

    void connectionException(TCPConnection tcpConnection,Exception e);

    void recieveMessageException(TCPConnection tcpConnection, Exception e);


}
