package controllers;


import Connector.TCPConnection;
import Connector.TCPConnectionListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.WrapperUser;
import javafx.scene.image.ImageView;
import sample.UsersManager;


import java.io.IOException;


public class CController implements TCPConnectionListener {

    @FXML
    private ImageView iSettings;
    @FXML
    private TableColumn cUsers;
    @FXML
    private TableView usersList;
    @FXML
    private TextField tfNickname;
    @FXML
    private Label lErrorMsg;
    @FXML
    private TextArea taLog;
    @FXML
    private TextField tfInput;
    @FXML
    private Label lConnectionStatus;

    private EventHandler<MouseEvent> me;
    private boolean tcpIsRun = false;
    private TCPConnection tcpConnection;
    private final String HOST = "127.0.0.1";
    private final int PORT = 8189;

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
    }

    public void sendMsg() {
        String nick = tfNickname.getText();
        String msg = tfInput.getText();
        if (!msg.equals("")) {
            if (!nick.equals("")) {
                String completeMsg = nick + ": " + msg;
                tcpConnection.sendMessage(completeMsg);
                tfInput.clear();
            } else {
                tfNickname.requestFocus();
                tfNickname.setPromptText("Нужно ввести никнейм");
            }
        }
    }

    public void initializeUserList() {
        cUsers.setCellValueFactory(new PropertyValueFactory<WrapperUser, String>("firstLastName"));
        usersList.setItems(UsersManager.getUsersList());
    }

    private void writeTaLog(String msg) {
        taLog.appendText(msg + "\n");
    }

    private void lConnectionStatusChanger(String value) {
        Platform.runLater(() -> lConnectionStatus.setText(value));
    }

    private void lErrorMsgChanger(String value) {
        Platform.runLater(() -> lErrorMsg.setText(value));
    }

    public void startConnection() {
        new Thread(() -> tcpConnection = new TCPConnection(HOST, PORT, this)).start();
    }

    @Override
    public void onConnection(TCPConnection tcpConnection) {
        tcpIsRun = true;
        lConnectionStatusChanger("Онлайн");
    }

    @Override
    public void onDisconnection(TCPConnection tcpConnection) {
        tcpIsRun = false;
        lConnectionStatusChanger("Оффлайн");
    }

    @Override
    public void onRecieveTextMessage(TCPConnection tcpConnection, String msg) {
        writeTaLog(msg);
    }

    @Override
    public void connectionException(Exception e) {
        lConnectionStatusChanger("Не удалось установить соединение");
    }

    @Override
    public void connectionException(TCPConnection tcpConnection, Exception e) {
        lConnectionStatusChanger("Не удалось установить соединение");
    }

    @Override
    public void recieveMessageException(TCPConnection tcpConnection, Exception e) {
        lErrorMsgChanger("Ошибка передачи сообщения " + e.getMessage());
    }

}
