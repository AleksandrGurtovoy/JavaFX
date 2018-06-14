/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx3dcube;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.DepthTest;
import javafx.animation.*;
import javafx.util.*;
import javafx.scene.paint.*;
import javafx.scene.layout.HBox;
import java.util.*;
import static javafx.application.Application.launch;

/**
 *
 * @author siede
 */
enum CubeRotation
{
    X,Y,Z
}
public class JavaFX3DCube extends Application {
    public static ArrayList<Timeline> animations = new ArrayList<Timeline>();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        Scene scene = new Scene(root);
        HBox cPlace = (HBox)root.lookup("#hBox1");
        cPlace.getChildren().add(create3dContent(Color.CYAN, CubeRotation.X));
        cPlace = (HBox)root.lookup("#hBox2");
        cPlace.getChildren().add(create3dContent(Color.VIOLET, CubeRotation.Y));
        cPlace = (HBox)root.lookup("#hBox3");
        cPlace.getChildren().add(create3dContent(Color.YELLOW, CubeRotation.Z));
        root.setDepthTest(DepthTest.ENABLE);
        scene.setCamera(new PerspectiveCamera());
        stage.setScene(scene);
        stage.show();

    }
    
    @Override public void stop() {
        for (Timeline entry : animations) {
            entry.stop();
        }
    } 


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Node create3dContent(Color color, CubeRotation rotation) {    
        Timeline animation = new Timeline();
        Cube c = new Cube(50,color);
        c.rx.setAngle(45);
        c.ry.setAngle(45);
        c.rz.setAngle(45);
        
        switch(rotation)
        {
            case X:
                animation.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, 
                    new KeyValue(c.rx.angleProperty(), 0d)),
                    new KeyFrame(Duration.seconds(5), 
                    new KeyValue(c.rx.angleProperty(), 360d)
                ));
                break;
            case Y:
                animation.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, 
                    new KeyValue(c.ry.angleProperty(), 0d)),
                    new KeyFrame(Duration.seconds(8), 
                    new KeyValue(c.ry.angleProperty(), 360d)
                ));
                break;
            case Z:
                animation.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, 
                    new KeyValue(c.rz.angleProperty(), 0d)),
                    new KeyFrame(Duration.seconds(2), 
                    new KeyValue(c.rz.angleProperty(), 360d)
                ));
                break;
        }
        
        animation.setCycleCount(Animation.INDEFINITE);
        animations.add(animation);
        return c.box;
    }

}
