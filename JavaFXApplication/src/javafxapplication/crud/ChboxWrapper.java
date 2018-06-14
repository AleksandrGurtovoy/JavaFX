/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication.crud;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Admin
 */
public class ChboxWrapper extends BorderPane{
        final VBox vbox = new VBox(); 
        
        public ChboxWrapper(CheckBox cb, String name, double width){
            this.styleProperty().set("-fx-background-color: #FFFFFF;-fx-border-color: lightgray;-fx-border-width: 1; -fx-border-style: solid");
            
            final Label label = new Label(name);
            label.setFont(new Font("Arial", 8));
            vbox.getChildren().add(label);
            vbox.getChildren().add(cb);
            vbox.setAlignment(Pos.CENTER);
            this.setCenter(vbox);
            this.setPrefWidth(width);
        }
}

