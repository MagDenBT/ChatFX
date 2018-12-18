package sample;

import controllers.CController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartClient  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/client.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Балона ВЗОРВАЛАСЬ");
        primaryStage.show();
        CController controller = loader.getController();


      //  UsersManager.addUser(new WrapperUser("magden","Muhammad", "Bilto"));
       // controller.initializeUserList();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
