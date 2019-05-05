package controllers;

import Statistic.IStatisticServer;
import Statistic.StatisticCollector;
import Statistic.StatisticListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Core.ServerWorker;
import Core.ServerWorkerListener;

import java.io.IOException;

public class SController implements ServerWorkerListener, StatisticListener {

    @FXML
    private Label lAuthConnection;
    @FXML
    private Label lServerStatus;
    @FXML
    private Label lFailedAuth;
    @FXML
    private Button butStopServer;
    @FXML
    private Button butStartServer;

    private ServerWorker worker;
    private IStatisticServer statisticServer;
    private final int PORT = 8199;
    private final long maxTimeUnAuthorized = 5000;
    private final long conInspectionTime = 10000;
    private final int maxCountFailedAuthor = 3;



    @FXML
    public void initialize() {
        lAuthConnection.setText("0");
        lFailedAuth.setText("0");

            statisticServer = StatisticCollector.getInstance();
            statisticServer.addListener(this);

                worker = new ServerWorker(PORT, maxTimeUnAuthorized, conInspectionTime, maxCountFailedAuthor, this);
    }

    @Override
    public void tcpConnectionCreateException(IOException e) {

    }

    @Override
    public void serverSocketCreateException(IOException e) {

    }

    @Override
    public void createExcWindowWithCloseApp(String error) {

    }

    @Override
    public void updateAuthorizedCount(int count) {
        Platform.runLater(() -> lAuthConnection.setText(Integer.toString(count)));
    }

    @Override
    public void updateUnauthorizedCount(int count) {
        Platform.runLater(() -> lFailedAuth.setText(Integer.toString(count)));
    }

    @FXML
    private void stopServer(ActionEvent event) {
    }

    @FXML
    private void startServer(ActionEvent event) {
    }
}
