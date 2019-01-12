package controllers;


import Core.DataSaver;
import Core.DataSaverListner;
import Core.Worker;
import Core.WorkerListener;
import UserList.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;



import java.io.IOException;

/**
 * Главное окно, где, собсвтенно и есть чат. Запускает 2 спутника в отдельных потоках, являясь их слушателем:
 * Worker - отвечает за работу с сервером
 * DataSaver - отвечает за сохранения и восстановление данных
 *
 */

public class CController implements WorkerListener,DataSaverListner {

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
    private final String profilFileName = "pr";
    private Worker worker;
    private DataSaver dataSaver;



    @FXML
    public void initialize() {
        new Thread(()-> worker = new Worker(CController.this, HOST,  PORT)).start();
        dataSaver = new DataSaver(this);//Инициализация Сохраняльщика
        iSettings.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)-> openProfilWindow());
        new Thread(()->{
            try {
                Thread.sleep(300);
                dataSaver.restoreProfil();
//                if(dataSaver.isProfilRestored()){
//                    worker.AuthOnServer(dataSaver.getUser());
//                }
            } catch (InterruptedException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            } catch (ClassNotFoundException e) {
                e.getMessage();
            }
        }).start();
    }

    /**
     * Отображение данных о пользователе
     * @param user
     */
    private void setProfilLabels(User user) {
        if (user != null) {
            iAvatar.setImage(user.getPhoto());
            lFirstLastName.setText(user.getFirstName() + " " +  user.getLastName());
        }
    }


    public void clickOnProfilGroup(MouseEvent mouseEvent) {
        openProfilWindow();
    }
/*
Генераторы окон/////////
 */

    /**
     * Открытие окна с с настройками профиля
     */
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
            signInController.setUser();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Окно "Error"
     * @param errorText
     */
    private synchronized void createErrorWindow(String errorText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Error.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.initOwner(taLog.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            ((ErrorController) loader.getController()).setlErrorText(errorText);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
/////////Генераторы окон
 */

    /**
     * Отправка текстового сообщения с поля набора текста на сервер
     */
    public void sendMsg() {
        String text = tfInput.getText();
        if (!text.equals(""))
                if(worker.sendTextMsg(text))
                     tfInput.clear();
    }



//    public void initializeUserList() {
//        cUsers.setCellValueFactory(new PropertyValueFactory<WrapperUser, String>("firstLastName"));
//        usersList.setItems(FriendsManager.getUsersList());
//    }

    /**
     * Запись пришедшего сообщения в поле в чат
     * т.к. этот контроллер - слушатель воркера, который работает в обдельном потоке
     * поэтому запись пришедшего сообщения реализавана через Platform.runLater.
     * P.S. Особенность работы потоков с GUI
     * @param msg
     */
    private void writeTaLog(String msg) {
        Platform.runLater(()-> taLog.appendText(msg + "\n"));

    }

    /**
     * Отоброжалка статуса соединения
     * @param value
     * @param color
     */
    private void lConnectionStatusChanger(String value, Color color) {
        Platform.runLater(() -> {
            lConnectionStatus.setText(value);
            lConnectionStatus.setTextFill(color);
        });
    }


    /*
    блок кода отвечающего за слушание событий с Worker ////////////////
     */

    /**
     * Воркер кидает сюда пользовательское сообщение от сервера
     * @param msg
     */
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

    /**
     * Вызывается из воркера если авторизация на сервере пройдена
     */
    @Override
    public void onSigned() {
        lConnectionStatusChanger("В сети", Color.GREEN);
    }

/*
//////////////// блок кода отвечающего за слушание событий с Worker
 */

    /*
    блок кода отвечающего за слушание событий с DataSaver ////////////////
     */

//    /**
//     * Получение инфы о пользовтеле после прочтения данных профиля
//     * и запуск авторизации на сервере
//     * @param user
//     */
//    @Override
//    public void restoredUser(User user) {
//        this.user = user;
//        worker.AuthOnServer(this.user);
//        setProfilLabels(this.user);
//    }

    /**
     * Ловим ошибку и передаем ее в генератор окна для отображения
     * @param message
     */
    @Override
    public void onException(String message) {
        createErrorWindow(message);
    }

    /**
     * Обновление инфы о пользовтеле в окне и на сервере после сохранения данных профиля
     */
    @Override
    public void ProfilUpdated() {
        worker.updateUserAtServer(dataSaver.getUser());
        setProfilLabels(dataSaver.getUser());
    }

      /*
    //////////////// блок кода отвечающего за слушание событий с DataSaver
     */

}
