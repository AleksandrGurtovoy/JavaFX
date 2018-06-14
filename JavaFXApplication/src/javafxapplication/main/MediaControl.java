/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication.main;

import java.io.File;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafxapplication.utils.FileFilterFx;

/**
 *
 * @author Admin
 */
public class MediaControl extends BorderPane {

     private MediaPlayer mp;
    private MediaView mediaView;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Label errorLabel;
    private Slider volumeSlider;
    private HBox mediaBar;
    private int fileIndex = -1;
    private final StackPane mvPane = new StackPane();
    private Label trackLabel = new Label();
    private Button playButton = new Button(">");
    private Button nextButton = new Button(">>");
    private Button prevButton = new Button("<<");
    
    public MediaControl(String mediaDir) {
        File dir = new File(mediaDir);
        final File[] files = dir.listFiles(new FileFilterFx());
        if (files.length == 0){
            errorLabel = new Label("No avalible files was found");
            errorLabel.setStyle("-fx-text-fill: #ff0000;");
            this.setCenter(errorLabel);
            return;
        } else{
            this.mp = new MediaPlayer(new Media(getNextFile(files)));
            setStyle("-fx-background-color: #bfc2c7;");
            changeMP();
            mvPane.setStyle("-fx-background-color: black;");
            setCenter(mvPane);
            mediaBar = new HBox();
            mediaBar.setAlignment(Pos.CENTER);
            mediaBar.setPadding(new Insets(5, 10, 5, 10));
            BorderPane.setAlignment(mediaBar, Pos.CENTER);
        }
        
        playButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    Status status = mp.getStatus();
                    if (status == Status.UNKNOWN || status == Status.HALTED) {
                        // don't do anything in these states
                        return;
                    }
                    if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
                        mp.play();
                    } else {
                        mp.pause();
                    }
                }
            });
            mediaBar.getChildren().add(playButton);

        prevButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    mp.stop();
                    mp = new MediaPlayer(new Media(getPrevFile(files)));
                    mp.play();
                    changeMP();
                }
            });
            mediaBar.getChildren().add(prevButton);
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    mp.stop();
                    mp = new MediaPlayer(new Media(getNextFile(files)));
                    mp.play();
                    changeMP();
                }
            });
            mediaBar.getChildren().add(nextButton);
        
            // Add spacer
            Label spacer = new Label("   ");
            mediaBar.getChildren().add(spacer);

            // Add audio file path label
            mediaBar.getChildren().add(trackLabel);

            // Add the volume label
            Label volumeLabel = new Label("Vol: ");
            mediaBar.getChildren().add(volumeLabel);

            // Add Volume slider
            volumeSlider = new Slider();
            volumeSlider.setPrefWidth(70);
            volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
            volumeSlider.setMinWidth(30);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (volumeSlider.isValueChanging()) {
                        mp.setVolume(volumeSlider.getValue() / 100.0);
                    }
                }
            });
            volumeSlider.setValue(100);
            mediaBar.getChildren().add(volumeSlider);
            setBottom(mediaBar);

    }
    
    
        private void changeMP(){
        mvPane.getChildren().remove(mediaView);
        mediaView = new MediaView(mp);
        mvPane.getChildren().add(mediaView);
        mvPane.setAlignment(mediaView, Pos.CENTER);
        trackLabel.setText("Track: " + mp.getMedia().getSource());
        stopRequested = false;
        setActions();
    }

        private void setActions(){
            mp.setOnPlaying(new Runnable() {
                public void run() {
                    if (stopRequested) {
                        mp.pause();
                        stopRequested = false;
                    } else {
                        playButton.setText("||");
                    }
                }
            });

            mp.setOnPaused(new Runnable() {
                public void run() {
                    playButton.setText(">");
                }
            });

            mp.setOnEndOfMedia(new Runnable() {
                public void run() {
                    playButton.setText("||");
                    stopRequested = true;
                    nextButton.fire();
                }
            });
    }

    public final String getNextFile(File[] files){
        if (this.fileIndex >= files.length-1)
            this.fileIndex = -1;
        return files[++this.fileIndex].toURI().toString();
    }
    
    public final String getPrevFile(File[] files){
        if (this.fileIndex <= 0)
            this.fileIndex = files.length;
        return files[--this.fileIndex].toURI().toString();
    }

    public void stop(){
        if (this.mp != null)
            this.mp.stop();
    }

    
   
   
}

