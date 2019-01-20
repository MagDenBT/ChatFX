package controllers;

import Core.DataSaver;
import UserList.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class SignInController {

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
    private TextField tfSex;
    @FXML
    private ImageView iSave;

    private DataSaver dataSaver;

    @FXML
    public void initialize() {
        //Adding implementation for SaveButton
        iSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            TextField fields[] = {tfLogin, tfPassword, tfFirstName, tfLastName, tfSex};
            boolean isEmpty = false;
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getText().equals("")) {
                    fields[i].setPromptText("НЕ ЗАПОЛЕНО");
                    isEmpty = true;
                }
            }
            if (!isEmpty) {
                User user = dataSaver.getUser();
                user.setLogin(tfLogin.getText());
                user.setPassword(tfPassword.getText());
                user.setFirstName(tfFirstName.getText());
                user.setLastName(tfLastName.getText());
                user.setSex(tfSex.getText());
                String age = tfAge.getText();
                user.setAge(age.equals("") ? 0 : Integer.valueOf(age));
                user.setPhoto(iPhoto.getImage());
                user.setPhotoExtention(iPhoto.getAccessibleText());
                try {
                    dataSaver.saveProfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //Adding implementation for PhotoChooser
        iPhoto.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> selectImage(event));
    }

    void setDataSaver(DataSaver dataSaver) {
        this.dataSaver = dataSaver;
    }

    void populateFields() {
        User user = dataSaver.getUser();
        tfLogin.setText(user.getLogin() != null ? user.getLogin() : "");
        tfPassword.setText(user.getPassword() != null ? user.getPassword() : "");
        tfFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
        tfLastName.setText(user.getLastName() != null ? user.getLastName() : "");
        tfSex.setText(user.getSex() != null ? user.getSex() : "");
        tfAge.setText(String.valueOf(user.getAge()));
        if (user.getPhoto() != null)
            iPhoto.setImage(user.getPhoto());
    }

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

    public void deletePhoto(ActionEvent event) {
        iPhoto.setImage(new Image("Assets/emptyPhoto.png"));
        iPhoto.setAccessibleText(null);
    }

}
