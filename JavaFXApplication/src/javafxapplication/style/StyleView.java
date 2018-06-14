package javafxapplication.style;

import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafxapplication.JavaFXApplication;
import javafxapplication.managers.GenericDAO;
import javafxapplication.models.UserFx;


public class StyleView extends BorderPane {

    private final static int rectW = 50;
    private final static int rectH = 100;
    private final static int imgW = 200;
    private final VBox vbox = new VBox();
    private final HBox hbox = new HBox();
    private final ListView<String> imgList = new ListView<String>();
    private final ListView<String> colorList = new ListView<String>();
    private final ChoiceBox cb = new ChoiceBox();
    private final UserFx loggedUser = JavaFXApplication.getInstance().loggedUser;
    private final ObservableList<String> colorData = FXCollections.observableArrayList("white", "chocolate", "salmon", "gold", "coral", "darkorchid", "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue", "blueviolet", "brown", "lightblue", "lightgreen", "lightgray", "gray");

    
    
    public StyleView(String ImgDir, String StyleDir) {
        final Label imageLabel = new Label("Select background image");
        final Label backLabel = new Label("Select base color");
        final Label styleLabel = new Label("Select style file");
        final ObservableList<String> imageData = getImagesData(ImgDir);
        final ObservableList<String> styleData = getData(StyleDir);
        final Button saveButton = new Button("Save as default");
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);
        imgList.setItems(imageData);
        colorList.setItems(colorData);

        imgList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ImageCell();
            }
        });
        colorList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ColorRectCell();
            }
        });
        imgList.setOrientation(Orientation.HORIZONTAL);
        colorList.setOrientation(Orientation.HORIZONTAL);
        colorList.setMaxWidth(rectW * 8);

        colorList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                changeStyle("-fx-background-image: url('" + loggedUser.getBackground() + "');-fx-background-position: center center; -fx-background-repeat: stretch;-fx-background-size: contain; -fx-base: " + new_val + ";");
                loggedUser.setBaseColor(new_val);
            }
        });

        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number new_value) {
                changeStyleSheet(styleData.get(new_value.intValue()));
                loggedUser.setStyle(styleData.get(new_value.intValue()));
            }
        });

        imgList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                if (new_val.contains("none.jpg")) {
                    changeStyle("-fx-background-image: url(''); -fx-base: " + loggedUser.getBaseColor() + ";");
                    loggedUser.setBackground("");
                } else {
                    changeStyle("-fx-background-image: url('" + new_val + "');-fx-background-position: center center; -fx-background-repeat: stretch;-fx-background-size: contain; -fx-base: " + loggedUser.getBaseColor() + ";");
                    loggedUser.setBackground(new_val);
                }
            }
        });

        for (int i = 0; i < styleData.size(); i++) {
            cb.getItems().add("Style " + i);
        }
        hbox.getChildren().addAll(styleLabel, cb, saveButton);
        vbox.getChildren().addAll(imageLabel, imgList, backLabel, colorList, hbox);
        this.setCenter(vbox);

        if (loggedUser.getStyle() != null && !loggedUser.getStyle().equals("")) {
            cb.getSelectionModel().select(styleData.indexOf(loggedUser.getStyle()));
        } else {
            cb.getSelectionModel().selectFirst();
        }

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                GenericDAO<UserFx> userManager = new GenericDAO(UserFx.class);
                userManager.update(loggedUser);
            }
        });

    }

    private void changeStyleSheet(String stylePath) {

        if (this.getScene() != null) {
            this.getScene().getStylesheets().clear();
            this.getScene().getStylesheets().add(stylePath);
        }
        
    }

    private void changeStyle(String style) {
        this.getScene().getRoot().setStyle(style);
    }

    private ObservableList<String> getData(String dirPath) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            CodeSource src = JavaFXApplication.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry ze = null;
                while ((ze = zip.getNextEntry()) != null) {
                    if (ze.getName().startsWith(dirPath) && (ze.getName().length() > dirPath.length())) {
                        list.add(ze.getName());
                    }
                }
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return list;
    }

     private ObservableList<String> getImagesData(String dirPath) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            CodeSource src = JavaFXApplication.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry ze = null;
                while ((ze = zip.getNextEntry()) != null) {
                    if (ze.getName().startsWith(dirPath) && (ze.getName().endsWith(".jpg")||ze.getName().endsWith(".png"))) {
                        list.add(ze.getName());
                    }
                }
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return list;
    }
    static class ColorRectCell extends ListCell<String> {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(rectW, rectH);
            if (item != null) {
                rect.setFill(Color.web(item));
                setGraphic(rect);
            }
        }
    }

    static class ImageCell extends ListCell<String> {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            try {
                ImageView iv = new ImageView();
                //item = item.replace("javafxapplication/", "");
                Image img = new Image(JavaFXApplication.class.getClassLoader().getResource(item).toString());
                iv.setImage(img);
                iv.setFitWidth(imgW);
                iv.setPreserveRatio(true);
                setGraphic(iv);
            } catch (Exception ex) {
                if (!empty) {
                    System.out.println();
                }
            }
        }
    }

}



