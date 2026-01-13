package main.java.de.uni_hannover.hci.maumau.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import main.java.de.uni_hannover.hci.maumau.lib.Baselib;

import java.io.File;

public class BaseController {


    /**
     * Das definiert Sounds Volume
     */
    public double volume = 20.00;
    public double xOffSet = 0;
    public double yOffSet = 0;
    public Stage stage;



    @FXML
    public Label labelClose, labelMin;

    /**
     * Instagram Channel des Projekts wird auf dem Chrome-Browser geöffnet.
     */
    @FXML
    public void handleInstagram(){
        Baselib.openBrowser(Baselib.getInstagram());
    }

    /**
     * Twitter Channel des Projekts wird auf dem Chrome-Browser geöffnet.
     */
    @FXML
    public void handleTwitter(){
        Baselib.openBrowser(Baselib.getTwitter());
    }

    /**
     * Facebook Channel des Projekts wird auf dem Chrome-Browser geöffnet.
     */
    @FXML
    public void handleFacebook(){
        Baselib.openBrowser(Baselib.getFacebook());
    }

    /**
     * Youtube Channel des Projekts wird auf dem Chrome-Browser geöffnet.
     */
    @FXML
    public void handleYoutube(){
        Baselib.openBrowser(Baselib.getYoutube());
    }

    /**
     * Das spielt eine Sound, beim button:hover.
     */
    @FXML
    public void onButton() {
        String path = "src/main/java/de/uni_hannover/hci/maumau/sounds/beep.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(this.volume / 100.00);
        mediaPlayer.setAutoPlay(true);
    }

    /**
     * Das bestimmt die x und y Achse von der Mouse beim Pressed.
     * @param event
     */
    @FXML
    public void handleMousePressed(MouseEvent event) {
        xOffSet = event.getSceneX();
        yOffSet = event.getSceneY();
    }

    /**
     * Solange die Mouse auf dem Zustand Dragged ist, wird die Stage bewegt und Opacity wird durch 0.8f ersetzt.
     * @param event
     */
    @FXML
    public void handleMouseDragged(MouseEvent event) {
        stage = (Stage)this.labelClose.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffSet);
        stage.setY(event.getScreenY() - yOffSet);
        stage.setOpacity(0.8f);
    }

    /**
     * Nach dem Zustand Dragged von der Mouse, wird Opacity durch 1.0f ersetzt.
     * @param event
     */
    @FXML
    public void handleDragDone(MouseEvent event) {
        stage = (Stage)this.labelClose.getScene().getWindow();
        stage.setOpacity(1.0f);
    }

    /**
     * Nach dem Zustand Realesed von der Mouse. wird Opacity durch 1.0f ersetzt.
     * @param event
     */
    @FXML
    public void handleMouseRealeased(MouseEvent event) {
        stage = (Stage)this.labelClose.getScene().getWindow();
        stage.setOpacity(1.0f);
    }

    /**
     * Stage wird minimiziert.(iconiziert)
     */
    @FXML
    public void handleMin() {
        Stage s = (Stage)this.labelClose.getScene().getWindow();
        s.setIconified(true);
    }
}
