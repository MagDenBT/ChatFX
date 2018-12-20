package controllers;

import Core.DataSaver;
import New.UserList.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.beans.EventHandler;

public class SignInController {

    @FXML
    private ImageView iRestore;
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
    public void initialize(){
      iSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)->{
          String login = tfLogin.getText();
          if(login.equals("")){
              tfLogin.requestFocus();
              tfLogin.setPromptText("ЭТО ПОЛЯ ОБЯЗАТЕЛЬНО");
          }else {

              dataSaver.saveProfil(new User(login));
          }

      });

      iRestore.addEventHandler(MouseEvent.MOUSE_CLICKED,(event)->{
          User user = dataSaver.restoreProfilFromFile();
          if (user != null) {
             tfLogin.setText(user.getLogin());
              tfPassword.setText(user.getPassword());
              tfFirstName.setText(user.getFirstName());
              tfLastName.setText(user.getLastName());
              tfAge.setText(String.valueOf(user.getAge()));
              tfSex.setText(user.getSex());
          }
      });
    }

     void setDataSaver(DataSaver dataSaver) {
        this.dataSaver = dataSaver;
    }
}
