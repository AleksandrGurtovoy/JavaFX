/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication.utils;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Admin
 */
public abstract class ButtonCreator {

public static Node createButton(String iconPath, final Runnable action) {
        final ImageView node = new ImageView(new Image(iconPath));
        node.setFitWidth(100);
        node.setPreserveRatio(true);

        node.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        action.run();
                    }
                });
        return node;
    }
}
