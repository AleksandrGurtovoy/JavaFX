package javafxapplication;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.Reflection;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxapplication.crud.CrudView;
import javafxapplication.models.UserFx;
import javafxapplication.login.Auth;
import javafxapplication.main.MediaControl;
import javafxapplication.utils.ButtonCreator;
import javafxapplication.browser.Browser;
import javafxapplication.chart.ChartView;
import javafxapplication.managers.GenericDAO;
import javafxapplication.style.StyleView;
import javafxapplication.utils.FileUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JavaFXApplication extends Application {

    private final int mainH = 600;
    private final int mainW = 1000;
    private final int loginH = 300;
    private final int loginW = 500;

    private StackPane view;
    private MediaControl mediaControl;

    private Stage stage;

    private static JavaFXApplication instance;
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaFXApplicationPU");
    public Logger logger;
    public UserFx loggedUser = null;

    public JavaFXApplication() {
        try {
            FileHandler hand = new FileHandler("log_info.log");
            logger = Logger.getLogger("log_file");
            logger.addHandler(hand);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        instance = this;
    }

    public static JavaFXApplication getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("JavaFX application");
        gotoLogin();

        stage.show();

    }

    public void userLogging(UserFx user) {
        this.loggedUser = user;
    }

    public UserFx userAuth(String username, String password) {
        return Auth.validate(username, password);
    }

    private void replaceSceneContent(String fxml, int width, int height) throws Exception {
        Parent page = (Parent) FXMLLoader.load(JavaFXApplication.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = stage.getScene();
        scene = new Scene(page, width, height);
        scene.getStylesheets().add(JavaFXApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public void gotoLogin() {
        try {
            replaceSceneContent("login/Login.fxml", loginW, loginH);
        } catch (Exception ex) {
            Logger.getLogger(JavaFXApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void userLogout() {
        this.loggedUser = null;
        gotoLogin();
    }

    public void gotoMain(Boolean accordion) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, mainW, mainH, Color.LIGHTBLUE);
        if (loggedUser.getStyle() != null) {
            scene.getStylesheets().add(loggedUser.getStyle());
        }
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        view = new StackPane();

        root.setCenter(view);
        setupGestureTarget(scene);

        if (accordion) {
            root.setLeft(getAccordion());
        } else {
            root.setBottom(getToolbar());
        }

        setBackgroundStyle(root, loggedUser.getBackground(), loggedUser.getBaseColor());

    }

    private void setupGestureTarget(final Scene target) {
        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    File file = db.getFiles().get(0);
                    if (file.isFile() && (file.getName().endsWith(".jpg")|| file.getName().endsWith(".png"))) {
                        try {
                            File localCopy = new File(UUID.randomUUID().toString() + '.' + FileUtils.extension(file.getName()));
                            FileUtils.copyFile(file, localCopy);
                            setBackgroundStyle(stage.getScene().getRoot(), localCopy.toURI().toString(), loggedUser.getBaseColor());
                            loggedUser.setBackground(localCopy.toURI().toString());
                            GenericDAO<UserFx> userManager = new GenericDAO(UserFx.class);
                            userManager.update(loggedUser);
                        } catch (IOException ex) {
                            Logger.getLogger(JavaFXApplication.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    event.setDropCompleted(true);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            }
        });
    }

    private void setBackgroundStyle(Node node, String url, String color) {
        node.setStyle("-fx-background-image: url('" + url + "');-fx-background-position: center center; -fx-background-repeat: stretch;-fx-background-size: contain;-fx-base: " + color + ";");
    }

    private Node getToolbar() {
        final HBox taskbar = new HBox(10);
        taskbar.setPadding(new Insets(10, 30, 40, 30));
        taskbar.setPrefHeight(150);
        taskbar.setAlignment(Pos.CENTER);
        setButtonsActions(taskbar);
        return taskbar;
    }

    private Node getAccordion() {
        final Accordion accordion = new Accordion();
        accordion.getPanes().addAll(
                new TitledPane("Media", getMediaButton()),
                new TitledPane("Browser", getBrowserButton()),
                new TitledPane("CRUD", getCrudButton()),
                new TitledPane("Charts", getChartButton()),
                new TitledPane("Toolbar", getToolsButton(false)),
                new TitledPane("Style", getStyleButton()),
                new TitledPane("Logout", getLogoutButton())
        );
        accordion.setExpandedPane(accordion.getPanes().get(0));
        return accordion;
    }

    private void changeView(Node node) {
        view.getChildren().clear();
        if (mediaControl != null) {
            mediaControl.stop();
        }
        view.getChildren().add(node);
    }

    private Node getMediaButton() {
        Node button = ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-0.png").toString(), new Runnable() {
            public void run() {
                //mediaControl = new MediaControl("D:\\Java\\playlist");
                mediaControl = new MediaControl("C:\\1.mp3");

                changeView(mediaControl);
            }
        });
        RotateTransition rotation = new RotateTransition(Duration.seconds(0.1), button);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.setByAngle(360);
        final Reflection effect = new Reflection();
        button.setEffect(effect);

        button.setOnMouseEntered(e -> {
            button.setEffect(null);
            rotation.play();
        });
        button.setOnMouseExited(e -> {
            button.setEffect(effect);
            rotation.pause();
        });
        return button;
    }

    private Node getBrowserButton() {
        return ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-3.png").toString(), new Runnable() {
            public void run() {
                Browser browser = new Browser();
                changeView(browser);
            }
        });
    }

    private Node getCrudButton() {
        return ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-2.png").toString(), new Runnable() {
            public void run() {
                changeView(new CrudView());
            }
        });
    }

    private Node getChartButton() {
        return ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-4.png").toString(), new Runnable() {
            public void run() {
                ChartView chartView = new ChartView();
                changeView(chartView);
            }
        });
    }

    private Node getLogoutButton() {
        Node button = ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-5.png").toString(), new Runnable() {
            public void run() {
                userLogout();
            }
        });
        return button;
    }

    private Node getStyleButton() {
        return ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-6.png").toString(), new Runnable() {
            public void run() {
                changeView(new StyleView("media/images/backgrounds/", "media/styles/"));
            }
        });
    }

    private Node getToolsButton(boolean flag) {
        class ToolbarChanging implements Runnable {

            private boolean toAccordion;

            public ToolbarChanging(boolean choise) {
                toAccordion = choise;
            }

            public void run() {
                gotoMain(toAccordion);
            }
        }
        return (ButtonCreator.createButton(getClass().getClassLoader().getResource("media/images/icon-1.png").toString(), new ToolbarChanging(flag) {
        }));
    }

    private void setButtonsActions(HBox taskbar) {

        taskbar.getChildren().add(getToolsButton(true));
        taskbar.getChildren().add(getMediaButton());
        taskbar.getChildren().add(getCrudButton());
        taskbar.getChildren().add(getBrowserButton());
        taskbar.getChildren().add(getChartButton());
        taskbar.getChildren().add(getStyleButton());
        taskbar.getChildren().add(getLogoutButton());

    }

}
