package main.java.de.uni_hannover.hci.maumau.controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.de.uni_hannover.hci.maumau.lib.Datapackage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kaan Kara
 * @version 0.0.1, 01 Juli 2021
 */
public class GameSceneController extends BaseController implements Initializable {

    public int id;
    public LoginController loginController;
    public boolean hasServer, wishChoosen = false;
    public ImageView selectedItem;
    public boolean selected = false;
    public boolean isGameStarted = false;

    @FXML
    public HBox cardZoneHBox, player1HBox, player2HBox, player3HBox, player4HBox, wishHBox;

    @FXML
    public ImageView playedCards;

    @FXML
    public Label labelServerName, labelPoint, labelUserName, labelNotification,labelYourTurn;

    @FXML
    public Button buttonSendMessage, buttonDraw, buttonMau, buttonWishSpade, buttonWishHeart, buttonWishDiamond, buttonWishClub;

    @FXML
    public TextField messageField;

    @FXML
    public TextArea messageArea;

    @FXML
    public void handleMau(){
        Datapackage data = new Datapackage("egal", false, id + "mau", "egal");
        this.loginController.client.sendMessage(data, 1000);
    }

    @FXML
    public void handleWishSpade(){
        Datapackage data = new Datapackage("egal", false, "set_wish", "spade");
        this.loginController.client.sendMessage(data, 300);
        wishChoosen = true;
    }

    @FXML
    public void handleWishHeart(){
        Datapackage data = new Datapackage("egal", false, "set_wish", "heart");
        this.loginController.client.sendMessage(data, 300);
        wishChoosen = true;
    }

    @FXML
    public void handleWishDiamond(){
        Datapackage data = new Datapackage("egal", false, "set_wish", "diamond");
        this.loginController.client.sendMessage(data, 300);
        wishChoosen = true;
    }

    @FXML
    public void handleWishClub(){
        Datapackage data = new Datapackage("egal", false, "set_wish", "club");
        this.loginController.client.sendMessage(data, 300);
        wishChoosen = true;
    }

    public void changeVisiblePropertforWishMenu(boolean visible){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                wishHBox.setVisible(visible);
            }
        });
    }

    @FXML
    public void handleSendMessage(){
        //to do

        Datapackage data = new Datapackage(this.loginController.client.username, false, "message_from_client_to_server", this.messageField.getText());
        System.out.println(data.getBefehl() + " " + data.getMessage() + " " + data.getName());
        this.loginController.client.sendMessage(data , 5000);
        this.messageField.clear();
    }

    public void changeTurn(boolean isMyTurn){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelYourTurn.setVisible(isMyTurn);
            }
        });
    }

    public void setHasServer(Boolean hasServer){
        this.hasServer = hasServer;
    }

    public void setGameStarted(){
        if(this.isGameStarted){
            this.isGameStarted = false;
        } else {
            this.isGameStarted = true;
        }
    }

    public void addMessageToArea(String msg){
        System.out.println("[GameController] " + msg);
        this.messageArea.appendText( msg + "\n");
    }

    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }

    public void setServerName(String serverName){
        this.labelServerName.setText(serverName);
    }

    public void setUserName(String username){
        this.labelUserName.setText(username);
    }

    public void setPoint(int p){
        this.labelPoint.setText(Integer.toString(p));
    }

    public void addCard(String card){
        InputStream stream;
        try{
            System.out.println("String: " + card);
            stream = new FileInputStream("src/main/java/de/uni_hannover/hci/maumau/images/cards/" + card + ".gif");
            Image image = new Image(stream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(45);
            imageView.setPreserveRatio(true);
            imageView.setId(card);

            imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(!(selectedItem == imageView)) {
                        imageView.setScaleX(1.3);
                        imageView.setScaleY(1.3);
                    }
                }
            });

            imageView.setOnMouseExited(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    if(!(selectedItem == imageView)) {
                        imageView.setScaleX(1.0);
                        imageView.setScaleY(1.0);
                    }
                }

            });

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    if(!(selectedItem == imageView) && selected) {

                        selectedItem.setScaleX(1);
                        selectedItem.setScaleY(1);
                        imageView.setScaleX(1.5);
                        imageView.setScaleY(1.5);
                        selectedItem = imageView;
                        if(selectedItem.getId().contains("11")){
                            changeVisiblePropertforWishMenu(true);
                        } else {
                            changeVisiblePropertforWishMenu(false);
                        }

                    } else if(!(selectedItem == imageView) & !selected) {

                        selectedItem = imageView;
                        selectedItem.setScaleX(1.5);
                        selectedItem.setScaleY(1.5);
                        selected = true;
                        if(selectedItem.getId().contains("11")){
                            changeVisiblePropertforWishMenu(true);
                        } else {
                            changeVisiblePropertforWishMenu(false);
                        }

                    } else {

                        selectedItem.setScaleX(1);
                        selectedItem.setScaleY(1);
                        selectedItem = null;
                        selected = false;
                        changeVisiblePropertforWishMenu(false);
                    }
                }

            });
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    player1HBox.getChildren().add(imageView);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOtherCard(int player){
        InputStream stream;
        try{
            stream = new FileInputStream("src/main/java/de/uni_hannover/hci/maumau/images/cards/back01.gif");
            Image image = new Image(stream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(45);
            imageView.setPreserveRatio(true);
            imageView.setId("back01");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(id == 0){
                        if (player == 0){

                        } else if (player == 1){
                            player2HBox.getChildren().add(imageView);
                        } else if (player == 2){
                            player3HBox.getChildren().add(imageView);
                        } else {
                            player4HBox.getChildren().add(imageView);
                        }
                    } else if (id == 1) {
                        if (player == 0){
                            player2HBox.getChildren().add(imageView);
                        } else if (player == 1){

                        } else if (player == 2){
                            player4HBox.getChildren().add(imageView);
                        } else {
                            player3HBox.getChildren().add(imageView);
                        }
                    } else if (id == 2){
                        if (player == 0){
                            player4HBox.getChildren().add(imageView);
                        } else if (player == 1){
                            player3HBox.getChildren().add(imageView);
                        } else if (player == 2){

                        } else {
                            player2HBox.getChildren().add(imageView);
                        }
                    } else if (id == 3) {
                        if (player == 0){
                            player3HBox.getChildren().add(imageView);
                        } else if (player == 1){
                            player4HBox.getChildren().add(imageView);
                        } else if (player == 2){
                            player2HBox.getChildren().add(imageView);
                        } else {

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCard(){
        System.out.println("[Client] Remove : " + this.selectedItem.getId());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                player1HBox.getChildren().remove(selectedItem);
            }
        });
    }

    public void removeOtherCard(int player){
        Platform.runLater(new Runnable() {
            public void run() {
                if(id == 0){
                    if (player == 0){
                        player1HBox.getChildren().remove(0);
                    } else if (player == 1){
                        player2HBox.getChildren().remove(0);
                    } else if (player == 2){
                        player3HBox.getChildren().remove(0);
                    } else {
                        player4HBox.getChildren().remove(0);
                    }
                } else if (id == 1) {
                    if (player == 0){
                        player2HBox.getChildren().remove(0);
                    } else if (player == 1){
                    } else if (player == 2){
                        player4HBox.getChildren().remove(0);
                    } else {
                        player3HBox.getChildren().remove(0);
                    }
                } else if (id == 2){
                    if (player == 0){
                        player4HBox.getChildren().remove(0);
                    } else if (player == 1){
                        player3HBox.getChildren().remove(0);
                    } else if (player == 2){

                    } else {
                        player2HBox.getChildren().remove(0);
                    }
                } else {
                    if (player == 0){
                        player3HBox.getChildren().remove(0);
                    } else if (player == 1){
                        player4HBox.getChildren().remove(0);
                    } else if (player == 2){
                        player2HBox.getChildren().remove(0);
                    } else {

                    }
                }
            }
        });
    }

    public void setNotification(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelNotification.setText(text);
                labelNotification.setVisible(true);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                labelNotification.setVisible(false);
                            }
                        },
                        3000
                );
            }
        });
    }

    @FXML
    public void playCard(){
        if(!isGameStarted){
            setNotification("Game is not started.");
        } else if(selected && isGameStarted){
            if(selectedItem.getId().contains("11")){
                if(wishChoosen){
                    Datapackage data = new Datapackage(this.loginController.client.username, false, this.id + "play_card", selectedItem.getId() + "," + player1HBox.getChildren().indexOf(selectedItem));
                    this.loginController.client.sendMessage(data, 1000);
                } else {
                    // TODO choice wish
                }
            } else {
                Datapackage data = new Datapackage(this.loginController.client.username, false, this.id + "play_card", selectedItem.getId() + "," + player1HBox.getChildren().indexOf(selectedItem));
                this.loginController.client.sendMessage(data, 1000);
            }
        } else if (!selected && isGameStarted) {
            setNotification("Select your card.");
        }
    }

    @FXML
    public void handleDraw(){
        Datapackage data = new Datapackage(this.loginController.client.username, false, this.id + "draw_card", "random");
        this.loginController.client.sendMessage(data , 1000);
    }

    public void chancePlayedCard(String card){
        InputStream stream;
        try{
            stream = new FileInputStream("src/main/java/de/uni_hannover/hci/maumau/images/cards/" + card + ".gif");
            Image image = new Image(stream);
            this.playedCards.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Das Programm wird beendet.
     */
    @FXML
    public void handleClose() {

        if (hasServer){
            this.loginController.client.close();
            this.loginController.server.close();
            Stage s = (Stage)this.labelClose.getScene().getWindow();
            s.close();
            System.exit(1);
        } else {
            this.loginController.client.close();
            Stage s = (Stage)this.labelClose.getScene().getWindow();
            s.close();
            System.exit(1);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
