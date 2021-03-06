package Core;

import Connector.TCPConnection;

import java.util.ArrayList;
/*
Основная задача этого объекта - это прочесываение списка неавторизованных соединений и по тайм-ауту
попытаться прибить соединение и после удалить его из наблюдаемого списка. Удаление из списка по проваленным попыткам авторизации
происходит в логике авторизации в ServerWorker

Также тут будут(возможно уже есть) методы для проверки на активность списка авторизованных соединений, также по тайм-ауту
 */
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
        ArrayList<TCPConnection> inspectedList = serverWorker.getUnAuthenticatedConnections();

        for (int i = 0; i < inspectedList.size(); i++) {
            long liveTime = currentTime - inspectedList.get(i).getTimeOfStartConnection();
            if(liveTime > maxLiveTime){
                inspectedList.get(i).closeConnection();
                inspectedList.remove(i);
            }
        }
    }

    public void connectionDestructor(TCPConnection tcpConnection, int countFailedAuthor) {
        if(countFailedAuthor >= maxCountFailedAuthor) {
            tcpConnection.closeConnection();
            serverWorker.getUnAuthenticatedConnections().remove(tcpConnection);
        }
    }

}
