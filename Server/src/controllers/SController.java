package controllers;

import New.Connector.TCPConnection;
import New.Connector.TCPConnectionListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.ServerWorker;
import sample.ServerWorkerListener;

import java.io.IOException;
import java.net.ServerSocket;

public class SController implements ServerWorkerListener {

    public Label lConnectionCount;
    public Label lServerStatus;
    public Label totStatius;
    private ServerWorker worker;
    private final int PORT = 8199;
    private final long maxTimeUnAuthorized = 5000;
    private final long conInspectionTime = 10000;
    private final int maxCountFailedAuthor = 3;


    @FXML
    public void initialize() {
        lConnectionCount.setText("0");
        worker = new ServerWorker(PORT, maxTimeUnAuthorized, conInspectionTime, maxCountFailedAuthor, this);
    }



    private void lConnectionCountChange(String value) {
        Platform.runLater(() -> {
            lConnectionCount.setText(value);
        });
    }


    @Override
    public void tcpConnectionCreateException(IOException e) {

    }

    @Override
    public void serverSocketCreateException(IOException e) {

    }
}
