/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx3dcube;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.animation.*;

/**
 *
 * @author siede
 */
public class ViewController implements Initializable {
    private boolean stop=false;
    
    @FXML 
    private void handleButtonAction(ActionEvent event) {
        Button button = (Button)event.getSource();
        if (stop){
            for(Timeline animation : JavaFX3DCube.animations)
            {
               button.setText("Start");
               animation.pause(); 
            }
                
            stop=false;
        } else {
            for(Timeline animation : JavaFX3DCube.animations){
               animation.play(); 
            }
            button.setText("Stop");
            stop=true;
        }
    }

    
    @FXML
    private Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}