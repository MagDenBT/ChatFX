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

    @FXML
    public void initialize(){
      iSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)->{
          String login = tfLogin.getText();
          if(login.equals("")){
              tfLogin.requestFocus();
              tfLogin.setPromptText("ЭТО ПОЛЯ ОБЯЗАТЕЛЬНО");
          }else {
              DataSaver dataSaver = new DataSaver();
              dataSaver.saveProfil(null, new User(login));
          }

      });

      iRestore.addEventHandler(MouseEvent.MOUSE_CLICKED,(event)->{
          DataSaver dataSaver = new DataSaver();
          tfLogin.setText(dataSaver.restoreProfilFromFile(null));
      });
    }


}
