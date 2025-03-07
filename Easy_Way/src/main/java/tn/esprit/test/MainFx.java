
package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UserSpace.fxml"));
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("easy_way");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    //..
}
