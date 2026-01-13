package main.java.de.uni_hannover.hci.maumau;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Das startet das Program mit Mainmenu.
 *
 * @author Kaan Kara
 * @version 0.0.2, 28 June 2021
 */
public class Main extends Application {

    public Stage primaryStage;
    public Parent root;
    public Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        window();
    }

    public void window(){
        try {
            root = FXMLLoader.load(getClass().getResource("view/LoginView.fxml"));
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/loginstylesheets.css").toExternalForm());
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
