package controllers;

import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.ConnectionListManager;

import java.io.IOException;
import java.net.ServerSocket;

public class SController implements TCPConnectionListener {

    public Label lConnectionCount;
    public Label lServerStatus;
    public Label totStatius;
    private ServerSocket serverSocket;
    private Thread connectionsLine;


    @FXML
    public void initialize() {
        lConnectionCount.setText("0");
    }


    public void startServer() {
        connectionsLine = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8189);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    new TCPConnection(serverSocket.accept(), SController.this);
                } catch (IOException e) {
                    connectionException(e);
                }
            }
        });
        connectionsLine.start();
    }

    private void lConnectionCountChange(String value) {
        Platform.runLater(() -> {
            lConnectionCount.setText(value);
        });
    }

    @Override
    public synchronized void onConnection(TCPConnection tcpConnection) {
        String count = Integer.toString(ConnectionListManager.addConnection(tcpConnection));
        lConnectionCountChange(count);
    }

    @Override
    public synchronized void onDisconnection(TCPConnection tcpConnection) {
        String count = Integer.toString(ConnectionListManager.deleteConnection(tcpConnection));
        lConnectionCountChange(count);
    }

    @Override
    public synchronized void onRecieveTextMessage(TCPConnection tcpConnection, String msg) {
        ConnectionListManager.sendTextMsgtoAll(msg);
    }

    @Override
    public synchronized void connectionException(Exception e) {

    }

    @Override
    public void connectionException(TCPConnection tcpConnection, Exception e) {

    }

    @Override
    public synchronized void recieveMessageException(TCPConnection tcpConnection, Exception e) {

    }

}
