package controllers;


import Core.DataSaver;
import Core.Worker;
import Core.WorkerListener;
import UserList.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.*;
import javafx.scene.image.ImageView;



import java.io.IOException;


public class CController implements WorkerListener {

    @FXML
    private Label lFirstLastName;
    @FXML
    private ImageView iAvatar;
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

    private final String HOST = "127.0.0.1";
    private final int PORT = 8199;
    private Worker worker;
    private DataSaver dataSaver;

    private User user;
    @FXML
    public void initialize() {

        if(!initDataSaver()) {
            user = dataSaver.getUser();
            toAuthOnServer();
            setProfilLabels(user);
        }

        new Thread(() -> worker = new Worker(CController.this)).start();
    }

    private void setProfilLabels(User user) {
        if (user != null) {
            iAvatar.setImage(user.getPhoto());
            lFirstLastName.setText(user.getFirstName() + " " +  user.getLastName());
            lFirstLastName.setTextFill(new Color(91,181f,245,0.2));
        }
    }

    public void clickOnProfilGroup(MouseEvent mouseEvent) {
        openProfilWindow();
    }

    private void openProfilWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/signIn.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            stage.initOwner(taLog.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            SignInController signInController = loader.getController();
            signInController.setDataSaver(dataSaver);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toAuthOnServer(){
        new Thread(()->{
            worker.startConnection(HOST,PORT,user.getLogin(), user.getPassword());
        }).start();
    }

    public void sendMsg() {
        String msg = tfInput.getText();
        if (!msg.equals(""))
                if(worker.sendTextMsg(null,msg))
                     tfInput.clear();
    }

    private boolean initDataSaver(){
        try {
            dataSaver = new DataSaver();
            return true;
        } catch (IOException e) {
            createErrorWindow(e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            createErrorWindow(e.getMessage());
            return false;
        }

    }

    private void createErrorWindow(String errorText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Error.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(taLog.getScene().getWindow());
            ((ErrorController) loader.getController()).setlErrorText(errorText);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeUserList() {
        cUsers.setCellValueFactory(new PropertyValueFactory<WrapperUser, String>("firstLastName"));
        usersList.setItems(FriendsManager.getUsersList());
    }

    private void writeTaLog(String msg) {
        Platform.runLater(()-> taLog.appendText(msg + "\n"));

    }

    private void lConnectionStatusChanger(String value, Color color) {
        Platform.runLater(() -> {
            lConnectionStatus.setText(value);
            lConnectionStatus.setTextFill(color);
        });
    }



    @Override
    public void gotTextMsg(String msg) {
        writeTaLog(msg);
    }


    @Override
    public void connectionException(Exception e) {
        lConnectionStatusChanger("Сервер недоступен",Color.RED);
    }

    @Override
    public void recieveMessageException(Exception e) {

    }

    @Override
    public void onConnection() {
        lConnectionStatusChanger("Не авторизован", Color.YELLOW);
    }

    @Override
    public void onDisconnection() {
        lConnectionStatusChanger("Оффлайн",Color.RED);
    }

    @Override
    public void signIn(boolean answer) {
        lConnectionStatusChanger("В сети", Color.GREEN);
    }



}
