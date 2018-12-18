package controllers;


import New.Connector.Message;
import New.Connector.TCPConnection;
import New.Connector.TCPConnectionListener;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.*;
import javafx.scene.image.ImageView;


import java.io.IOException;


public class CController implements WorkerListener {

    @FXML
    private ImageView iSettings;
    @FXML
    private TableColumn cUsers;
    @FXML
    private TableView usersList;
    @FXML
    private TextField tfNickname;
    @FXML
    private TextArea taLog;
    @FXML
    private TextField tfInput;
    @FXML
    private Label lConnectionStatus;

    private EventHandler<MouseEvent> me;
    private final String HOST = "127.0.0.1";
    private final int PORT = 8189;
//    private final Thread workerThread;
    private Worker worker;

    @FXML
    public void initialize() {
        lConnectionStatus.setText("Подключаюсь к серверу");
        me = (event) -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/signIn.fxml"));
            try {
                loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.getRoot()));
                stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        iSettings.addEventHandler(MouseEvent.MOUSE_CLICKED, me);

       new Thread(() -> {
            worker = new Worker(CController.this, HOST, PORT, "testLogin", "testPassword");
        }).start();

    }

    public void sendMsg() {
        String msg = tfInput.getText();
        if (!msg.equals(""))
                if(worker.sendTextMsg(null,msg))
                     tfInput.clear();
    }

    public void initializeUserList() {
        cUsers.setCellValueFactory(new PropertyValueFactory<WrapperUser, String>("firstLastName"));
        usersList.setItems(UsersManager.getUsersList());
    }

    private void writeTaLog(String msg) {
        Platform.runLater(()-> taLog.appendText(msg + "\n"));

    }

    private void lConnectionStatusChanger(String value) {
        Platform.runLater(() -> lConnectionStatus.setText(value));
    }



    @Override
    public void gotTextMsg(String msg) {
        writeTaLog(msg);
    }


    @Override
    public void connectionException(Exception e) {
        lConnectionStatusChanger("Сервер недоступен");
    }

    @Override
    public void recieveMessageException(Exception e) {

    }

    @Override
    public void onConnection() {
        lConnectionStatusChanger("Не авторизован");
    }

    @Override
    public void onDisconnection() {
        lConnectionStatusChanger("Оффлайн");
    }

    @Override
    public void signIn(boolean answer) {
        lConnectionStatusChanger("В сети");
    }



}
