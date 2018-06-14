/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx3dcube;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.transform.Rotate;

/**
 *
 * @author dnepr
 */
public class Cube extends Group {
        final Rotate rx = new Rotate(0,Rotate.X_AXIS);
        final Rotate ry = new Rotate(0,Rotate.Y_AXIS);
        final Rotate rz = new Rotate(0,Rotate.Z_AXIS);
        public Box box;
        public Cube(double size, Color color) {

        box = new Box(size, size, size);
        box.setMaterial(new PhongMaterial(color));
        box.getTransforms().addAll(rz, ry, rx);
        }
    }