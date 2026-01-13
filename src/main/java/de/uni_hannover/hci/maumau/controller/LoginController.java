package main.java.de.uni_hannover.hci.maumau.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.de.uni_hannover.hci.maumau.client.Client;
import main.java.de.uni_hannover.hci.maumau.server.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Kaan Kara
 * @version 0.0.1, 01 Juli 2021
 */
public class LoginController extends  BaseController implements Initializable {

    public Server server;
    public Client client;
    public Parent root;
    public GameSceneController gameSceneController;
    public Stage stage;
    public Scene scene;
    public ObservableList<String> gameMods = FXCollections.observableArrayList("1 Player", "2 Player", "3 Player", "4 Player");

    @FXML
    public ListView<String> serverListView;

    @FXML
    public ComboBox<String> comboBoxGameMods;

    @FXML
    public TextField playerName, serverName, playerNameJoin;

    @FXML
    public PasswordField serverPass;

    @FXML
    public CheckBox serverPassCheck;

    @FXML
    public Label labelClose1, labelMin1, labelVolume;

    /**
     * Es ist AnchorPane, die auf der linken Seite steht. Es enthält welcomePane und rulesPane.
     */
    @FXML
    public AnchorPane defaultPane;

    /**
     * Es ist AnchorPane, die auf der rechten Seite steht. Es enthält mainMenuVBox,joinGameVBox, newGameVBox und settingsVBox.
     */
    @FXML
    public AnchorPane contentPane;

    /**
     * Nach der Start wird es als default auf der linken Seite angezeigt.
     */
    @FXML
    public AnchorPane welcomePane;

    /**
     * Unter der join game menu, new game menu und settings wird es auf der rechten Seite angezeigt.
     */
    @FXML
    public AnchorPane rulesPane;

    /**
     * Es wird nach der Start des Programs als default angezeigt.
     */
    @FXML
    public VBox mainMenuVBox;

    /**
     * Das enthält benötigte Komponente um einen Server zu joinen.
     */
    @FXML
    public VBox joinGameVBox;

    /**
     * Das enthält benötigte Komponente um einen Server mit seinem Owner-Client zu erstellen.
     */
    @FXML
    public VBox newGameVBox;

    /**
     * GameSettings kann unter diser VBox eingestellt werden. (z.B Soundsvolume)
     */
    @FXML
    public VBox settingsVBox;

    @FXML
    public Slider sliderVolume;

    @FXML
    public Button btnNewGameStart, btnJoinGameStart;

    /**
     * Das definiert die Zustand von Panes.
     */
    public boolean isChanced = false;

    /**
     * Das ändert sich die Position von defaultPane und contentPane miteinander.
     */
    public void chancePanes(){
        if(!isChanced){
            TranslateTransition tr = new TranslateTransition();
            tr.setDuration(Duration.seconds(1.7));
            tr.setNode(defaultPane);
            tr.setToX(350);
            TranslateTransition tr2 = new TranslateTransition();
            tr2.setDuration(Duration.seconds(1.7));
            tr2.setNode(contentPane);
            tr2.setToX(-275);
            tr.play();
            tr2.play();
            isChanced = true;
        } else {
            TranslateTransition tr = new TranslateTransition();
            tr.setDuration(Duration.seconds(1.7));
            tr.setNode(defaultPane);
            tr.setToX(0);
            TranslateTransition tr2 = new TranslateTransition();
            tr2.setDuration(Duration.seconds(1.7));
            tr2.setNode(contentPane);
            tr2.setToX(0);
            tr.play();
            tr2.play();
            isChanced = false;
        }
    }


    /**
     * open new game menu
     */
    @FXML
    public void handleNewGame(){
        chancePanes();
        welcomePane.setVisible(false);
        labelClose.setVisible(false);
        labelMin.setVisible(false);
        labelClose1.setVisible(true);
        labelMin1.setVisible(true);
        mainMenuVBox.setVisible(false);
        newGameVBox.setVisible(true);
        rulesPane.setVisible(true);
    }

    /**
     * open join game menu
     */
    @FXML
    public void handleJoinGame(){
        chancePanes();
        welcomePane.setVisible(false);
        labelClose.setVisible(false);
        labelMin.setVisible(false);
        labelClose1.setVisible(true);
        labelMin1.setVisible(true);
        mainMenuVBox.setVisible(false);
        joinGameVBox.setVisible(true);
        rulesPane.setVisible(true);
        ObservableList<String> serverList = FXCollections.observableArrayList(Server.getNames());
        this.serverListView.setItems(serverList);
    }

    /**
     * open settings menu
     */
    @FXML
    public void handleSettings(){
        chancePanes();
        welcomePane.setVisible(false);
        labelClose.setVisible(false);
        labelMin.setVisible(false);
        labelClose1.setVisible(true);
        labelMin1.setVisible(true);
        mainMenuVBox.setVisible(false);
        settingsVBox.setVisible(true);
        rulesPane.setVisible(true);
    }

    /**
     * turn back to main menu
     */
    @FXML
    public void handleBackToMainMenu(){
        chancePanes();
        welcomePane.setVisible(true);
        labelClose.setVisible(true);
        labelMin.setVisible(true);
        labelClose1.setVisible(false);
        labelMin1.setVisible(false);
        mainMenuVBox.setVisible(true);
        joinGameVBox.setVisible(false);
        newGameVBox.setVisible(false);
        settingsVBox.setVisible(false);
        rulesPane.setVisible(false);
    }

    @FXML
    public void handleNewGameStart(){
        try {
            // Scene chance
            // FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("../view/GameView.fxml"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginController.class.getResource("/main/java/de/uni_hannover/hci/maumau/view/GameView.fxml"));
            root = loader.load();
            this.gameSceneController = loader.getController();
            this.gameSceneController.setLoginController(this);
            this.gameSceneController.setServerName(this.serverName.getText());
            this.gameSceneController.setUserName(this.playerName.getText());
            this.gameSceneController.setHasServer(true);
            this.gameSceneController.volume = this.volume;
            stage = (Stage) this.labelClose.getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/main/java/de/uni_hannover/hci/maumau/css/loginstylesheets.css").toExternalForm());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            // ######################

            // Server erstellen
            if(this.serverPassCheck.isSelected()){
                server = new Server(this.serverName.getText(), this.serverPass.getText(),  this.comboBoxGameMods.getSelectionModel().getSelectedIndex() + 1);
            } else {
                server = new Server(this.serverName.getText(),this.comboBoxGameMods.getSelectionModel().getSelectedIndex() + 1);
            }

            // Client erstellen
            client = new Client("localhost", server.port, this.playerName.getText());
            client.setGameController(this.gameSceneController);
            client.setLoginController(this);
            if(this.comboBoxGameMods.getSelectionModel().getSelectedIndex() + 1 == 3){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameSceneController.player3HBox.setVisible(true);
                    }
                });
            } else if (this.comboBoxGameMods.getSelectionModel().getSelectedIndex() + 1 == 4){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameSceneController.player3HBox.setVisible(true);
                        gameSceneController.player4HBox.setVisible(true);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleJoinGameStart(){
        try {
            // Scene chance
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginController.class.getResource("/main/java/de/uni_hannover/hci/maumau/view/GameView.fxml"));
            root = loader.load();
            this.gameSceneController = loader.getController();
            this.gameSceneController.setLoginController(this);
            this.gameSceneController.setUserName(this.playerNameJoin.getText());
            this.gameSceneController.setServerName(this.serverListView.getSelectionModel().getSelectedItem().toString());
            this.gameSceneController.setHasServer(false);
            this.gameSceneController.volume = this.volume;
            stage = (Stage) this.labelClose.getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/main/java/de/uni_hannover/hci/maumau/css/loginstylesheets.css").toExternalForm());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            // ######################

            // Client erstellen
            client = new Client("localhost", Server.getPortfromFile(this.serverListView.getSelectionModel().getSelectedItem().toString()), this.playerNameJoin.getText());
            client.setGameController(this.gameSceneController);
            client.setLoginController(this);
            if(Server.getCapacityfromFile(this.serverListView.getSelectionModel().getSelectedItem().toString()) == 3){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameSceneController.player3HBox.setVisible(true);
                        gameSceneController.player4HBox.setVisible(true);
                    }

                });
            } else if (Server.getCapacityfromFile(this.serverListView.getSelectionModel().getSelectedItem().toString()) == 4){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameSceneController.player3HBox.setVisible(true);
                        gameSceneController.player4HBox.setVisible(true);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessageToArea(String msg){
        System.out.println("[LoginController] " + msg);
        this.gameSceneController.messageArea.appendText( msg + "\n");
    }

    @FXML
    public void handlePassCheck(){
        if(!this.serverPassCheck.isSelected()){
            this.serverPass.setDisable(true);
        } else {
            this.serverPass.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxGameMods.setItems(gameMods);
        comboBoxGameMods.setPromptText("Please Select Game Mod");
        this.sliderVolume.setValue(50);
    }

    @FXML
    public void handleVolume(){
        this.volume = sliderVolume.getValue();
        if(this.sliderVolume.getValue() == 0) {
            labelVolume.setText("Music Off");
        } else {
            labelVolume.setText("Music On");
        }
    }

    /**
     * Das Programm wird beendet.
     */
    @FXML
    public void handleClose() {
        Stage s = (Stage)this.labelClose.getScene().getWindow();
        s.close();
        System.exit(1);
    }
}
