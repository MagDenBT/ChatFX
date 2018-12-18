package sample;

import New.Connector.TCPConnection;

import java.util.ArrayList;

public class ConnectionCloser {

    private ServerWorker serverWorker;
    private Thread inspectorThread;
    private long inspectionTime;

    public ConnectionCloser(ServerWorker serverWorker, long inspectionTime) {
        this.serverWorker = serverWorker;
        this.inspectionTime = inspectionTime;
        inspectorThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(inspectionTime);

                    for (TCPConnection conection: serverWorker.getUnAuthorizedConnections()) {

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void inspector(){
        ArrayList<TCPConnection> inspectedList = serverWorker.getUnAuthorizedConnections();
        for (int i = 0; i < inspectedList.size(); i++) {
            inspectedList.get(i).getTimeOfStartConnection();
        }
    }
}
