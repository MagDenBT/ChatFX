package controllers;

import Core.DataSaver;
import Core.DataSaverListner;
import Core.Worker;
import Core.WorkerListener;
import UserList.Message;
import UserList.Sex;
import UserList.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.print.Collation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SignUpController implements WorkerListener, DataSaverListner {

    @FXML
    private ImageView iCancel;
    @FXML
    private ImageView iPhoto;
    @FXML
    private TextField tfLogin;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfAge;
    @FXML
    private ComboBox<Sex> cbSex;
    @FXML
    private ImageView iSave;
    @FXML
    private Label lRegisterStatus;

    private DataSaver dataSaver;
    private Worker worker;




    @FXML
    public void initialize() {
        worker = Worker.getInstance();
        worker.addListener(this);

        cbSex.getItems().addAll(Sex.values());
        iPhoto.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> selectImage(event));
    }


//    void populateFields() {
//        User user = dataSaver.getUser();
//        tfLogin.setText(user.getLogin() != null ? user.getLogin() : "");
//        tfPassword.setText(user.getPassword() != null ? user.getPassword() : "");
//        tfFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
//        tfLastName.setText(user.getLastName() != null ? user.getLastName() : "");
//        Sex sex = user.getSex();
//        if (sex != null)
//            cbSex.setValue(user.getSex());
//        tfAge.setText(String.valueOf(user.getAge()));
//        if (user.getPhoto() != null)
//            iPhoto.setImage(user.getPhoto());
//    }

    private void selectImage(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите фото");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Фото", "*.jpeg", "*jpg")
        );
        File choosedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (choosedFile != null) {
            String url = choosedFile.toURI().toString();
            iPhoto.setImage(new Image(url));
            iPhoto.setAccessibleText(url.substring(url.lastIndexOf('.') + 1));
        }
    }

    @FXML
    private void deletePhoto(ActionEvent event) {
        iPhoto.setImage(new Image("Assets/emptyPhoto.png"));
        iPhoto.setAccessibleText(null);
    }

    @FXML
    private void setCBSex(ActionEvent event) {
    }

    @FXML
    private void toRegisterOnServer(MouseEvent event) {
        if (checkPopulateUserFields()) {
            User user = createUser();
            if(worker.connectionIsLive()) {
                dataSaver = DataSaver.getInstance();
                dataSaver.addListener(this);
                worker.sendAuthentication(user);
                try {
                    dataSaver.saveProfile(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                ///дописать что делать если нет подключения
            }
        }

    }

    public void closeWindow(MouseEvent event) {
        Node sourse = (Node) (event.getSource());
        ((Stage) sourse.getScene().getWindow()).close();
    }

    private boolean checkPopulateUserFields() {
        TextField fields[] = {tfLogin, tfPassword, tfFirstName, tfLastName};
        boolean isEmpty = false;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().equals("")) {
                fields[i].setPromptText("НЕ ЗАПОЛЕНО");
                isEmpty = true;
            }
        }
        if (cbSex.getValue() == null)
            isEmpty = true;
        return isEmpty;
    }

    private User createUser() {
        User user = new User();
        user.setLogin(tfLogin.getText());
        user.setPassword(tfPassword.getText());
        user.setFirstName(tfFirstName.getText());
        user.setLastName(tfLastName.getText());
        user.setSex(Sex.MAN);
        user.setSex(cbSex.getValue());
        String age = tfAge.getText();
        user.setAge(age.equals("") ? 0 : Integer.valueOf(age));
        user.setPhoto(iPhoto.getImage());
        user.setPhotoExtention(iPhoto.getAccessibleText());
        return user;
    }

    @Override
    public void onException(String message) {

    }

    @Override
    public void ProfilUpdated() {

    }

    @Override
    public void gotTextMsg(String msg) {

    }

    @Override
    public void connectionException(Exception e) {

    }

    @Override
    public void recieveMessageException(Exception e) {

    }

    @Override
    public void onConnection() {

    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onSigned(Message msg) {

    }

    @Override
    public void onRegistration(Message msg) {
        String textStatus;
        Color color;
        if (msg.authenticated()){
            textStatus = "Регистрация прошла успешно";
            textStatus += "\n Сообщение сервера: " + msg.getTextMsg();
            color = Color.GREEN;
        }else{
            textStatus = "Не удалось зарегистрироваться";
            textStatus += "\n Сообщение сервера: " + msg.getTextMsg();
            color = Color.RED;
        }
       setlRegisterStatus(textStatus, color);
    }

    private void setlRegisterStatus(String text, Color color) {
        Platform.runLater(()->{
            lRegisterStatus.setText(text);
            lRegisterStatus.setTextFill(color);
        });
    }
    @FXML
    private void lRegStatusAction(MouseEvent event) {
    }


}
