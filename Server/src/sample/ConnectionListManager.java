package sample;

import Connector.TCPConnection;

import java.util.ArrayList;

public abstract class ConnectionListManager {

    private static final ArrayList<TCPConnection> connectionList = new ArrayList();

    public static int addConnection(TCPConnection tcpConnection) {
        connectionList.add(tcpConnection);
        return connectionList.size();
    }

    public static int deleteConnection(TCPConnection tcpConnection) {
        connectionList.remove(tcpConnection);
        return connectionList.size();
    }

    public static void sendTextMsgtoAll(String msg) {
        for (TCPConnection conn:connectionList
             ) {
            conn.sendMessage(msg);
        }
    }

    @Deprecated
    public static ArrayList<?> getList(){
        return connectionList;
    }

}
