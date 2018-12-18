package sample;

import New.Connector.TCPConnection;

import java.util.ArrayList;

public class ConnectionsInspector {

    private final ServerWorker serverWorker;
    private final Thread inspectorThread;
    private final long inspectionTime;
    private final long maxLiveTime;
    private final int maxCountFailedAuthor;

    public ConnectionsInspector(ServerWorker serverWorker, int maxCountFailedAuthor,long inspectionTime, long maxLiveTime) {
        this.serverWorker = serverWorker;
        this.maxCountFailedAuthor = maxCountFailedAuthor;
        this.inspectionTime = inspectionTime;
        this.maxLiveTime = maxLiveTime;
        inspectorThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(inspectionTime);
                    inspector();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void inspector(){
        long currentTime = System.currentTimeMillis();
        ArrayList<TCPConnection> inspectedList = serverWorker.getUnAuthorizedConnections();

        for (int i = 0; i < inspectedList.size(); i++) {
            long liveTime = currentTime - inspectedList.get(i).getTimeOfStartConnection();
            if(liveTime > maxLiveTime){
                inspectedList.get(i).closeConnection();
                inspectedList.remove(i);
            }
        }
    }

    public void conncetionDestructor(TCPConnection tcpConnection, int countFailedAuthor) {
        if(countFailedAuthor >= maxCountFailedAuthor) {
            tcpConnection.closeConnection();
            serverWorker.getUnAuthorizedConnections().remove(tcpConnection);
        }
    }

}
