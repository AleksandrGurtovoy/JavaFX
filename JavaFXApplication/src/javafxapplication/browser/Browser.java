package javafxapplication.browser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends BorderPane {

    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();
    private final HBox hbox = new HBox();
    private final TextField tf = new TextField();
    private final String startPage = "http://www.google.ru/";
    private final Button change = new Button("Change");

    public Browser() {
        this.setCenter(browser);
        tf.setText(startPage);
        tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    webEngine.load(tf.getText());
                }
            }
        });
        change.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                webEngine.load(tf.getText());
            }
        });
        browser.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                tf.setPrefWidth((Double) newVal - 61);
//At start change.getWidth() equals 0, so...hardcoding.
            }
        });
        hbox.getChildren().addAll(tf, change);
        this.setBottom(hbox);
        change.fire();

    }
}
