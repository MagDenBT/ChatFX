package Core;

import controllers.SController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/server.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
      //  SController controller = loader.getController();
    }
}
