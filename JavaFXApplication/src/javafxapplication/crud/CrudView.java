/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication.crud;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafxapplication.JavaFXApplication;
import javafxapplication.managers.GenericDAO;
import javafxapplication.models.UserFx;

/**
 *
 * @author Admin
 */
public class CrudView extends BorderPane {

    private final HBox hbox = new HBox();
    private final HBox buttonsBox = new HBox();
    private final VBox vbox = new VBox();
    private TableView<UserFx> table = new TableView();
    private final GenericDAO<UserFx> userManager = new GenericDAO(UserFx.class);
    private ObservableList<UserFx> data = FXCollections.observableArrayList();

    public CrudView() {

        TableColumn username = new TableColumn("Username");
        username.setCellValueFactory(new PropertyValueFactory<UserFx, String>("username"));

        TableColumn phone = new TableColumn("Phone");
        phone.setCellValueFactory(new PropertyValueFactory<UserFx, String>("phone"));

        TableColumn country = new TableColumn("Country");
        country.setCellValueFactory(new PropertyValueFactory<UserFx, String>("country"));

        TableColumn city = new TableColumn("City");
        city.setCellValueFactory(new PropertyValueFactory<UserFx, String>("city"));

        Callback<TableColumn<UserFx, Boolean>, TableCell<UserFx, Boolean>> booleanCellFactory = new Callback<TableColumn<UserFx, Boolean>, TableCell<UserFx, Boolean>>() {
            @Override
            public TableCell<UserFx, Boolean> call(TableColumn<UserFx, Boolean> p) {
                return new BooleanCell();
            }
        };

        TableColumn admin = new TableColumn("Admin");
        admin.setCellValueFactory(new PropertyValueFactory<UserFx, Boolean>("admin"));

        admin.setCellValueFactory(new PropertyValueFactory<UserFx, Boolean>("admin"));
        admin.setCellFactory(booleanCellFactory);

        TableColumn banned = new TableColumn("Banned");
        banned.setCellValueFactory(new PropertyValueFactory<UserFx, Boolean>("banned"));

        banned.setCellValueFactory(new PropertyValueFactory<UserFx, Boolean>("banned"));
        banned.setCellFactory(booleanCellFactory);

        Callback<TableColumn, TableCell> cellFactory
                = new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };

        username.setCellFactory(cellFactory);
        phone.setCellFactory(cellFactory);
        country.setCellFactory(cellFactory);
        city.setCellFactory(cellFactory);

        data = FXCollections.observableArrayList(userManager.getAll());
        table.setItems(data);
        table.getColumns().addAll(username, phone, country, city, admin, banned);
        this.setCenter(table);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        UserFx loggedUser = JavaFXApplication.getInstance().loggedUser;

        if (!loggedUser.getAdmin()) {
            return;
        }

        table.setEditable(true);

        phone.setOnEditCommit(new EventHandler<CellEditEvent<UserFx, String>>() {
            @Override
            public void handle(CellEditEvent<UserFx, String> t) {
                if (!checkPhoneOnlyDigits(t.getNewValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Phone number can contain only digits.");
                    alert.show();
                    return;
                }
                ((UserFx) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setPhone(t.getNewValue());
            }
        });

        country.setOnEditCommit(new EventHandler<CellEditEvent<UserFx, String>>() {
            @Override
            public void handle(CellEditEvent<UserFx, String> t) {
                ((UserFx) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setCountry(t.getNewValue());
            }
        });

        city.setOnEditCommit(new EventHandler<CellEditEvent<UserFx, String>>() {
            @Override
            public void handle(CellEditEvent<UserFx, String> t) {
                ((UserFx) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setCity(t.getNewValue());
            }
        });

        username.setOnEditCommit(new EventHandler<CellEditEvent<UserFx, String>>() {
            @Override
            public void handle(CellEditEvent<UserFx, String> t) {
                ((UserFx) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setUsername(t.getNewValue());
            }
        });

        banned.setOnEditCommit(new EventHandler<CellEditEvent<UserFx, Boolean>>() {
            @Override
            public void handle(CellEditEvent<UserFx, Boolean> t) {
                ((UserFx) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setBanned(t.getNewValue());
            }
        });

        admin.setOnEditCommit(new EventHandler<CellEditEvent<UserFx, Boolean>>() {
            @Override
            public void handle(CellEditEvent<UserFx, Boolean> t) {
                ((UserFx) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setAdmin(t.getNewValue());
            }
        });

        final CheckBox chAdmin = new CheckBox();
        final ChboxWrapper chAdmWrapper = new ChboxWrapper(chAdmin, "Admin", admin.getWidth());
        admin.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                chAdmWrapper.setPrefWidth((Double) newVal);
            }
        });

        final CheckBox chBanned = new CheckBox();
        final ChboxWrapper chBanWrapper = new ChboxWrapper(chBanned, "Banned", banned.getWidth());
        banned.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                chBanWrapper.setPrefWidth((Double) newVal);
            }
        });

        final TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setPrefWidth(username.getWidth());

        final TextField addPhone = new TextField();
        addPhone.setPromptText("Phone");
        addPhone.setPrefWidth(phone.getWidth());

        final TextField addCity = new TextField();
        addCity.setPromptText("City");
        addCity.setPrefWidth(city.getWidth());

        final TextField addCountry = new TextField();
        addCountry.setPromptText("Country");
        addCountry.setPrefWidth(country.getWidth());

        username.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                addUsername.setPrefWidth((Double) newVal);
            }
        });

        phone.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                addPhone.setPrefWidth((Double) newVal);
            }
        });

        city.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                addCity.setPrefWidth((Double) newVal);
            }
        });

        country.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                addCountry.setPrefWidth((Double) newVal);
            }
        });

        hbox.getChildren().addAll(addUsername, addPhone, addCountry, addCity, chAdmWrapper, chBanWrapper);
        vbox.getChildren().add(hbox);
        this.setBottom(vbox);

        final Button delBut = new Button("Delete user");
        final Button addBut = new Button("Add user");
        final Button saveBut = new Button("Save users");

        addBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(addUsername.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Username cannot be empty");
                    alert.show();
                    return;
                }
                if(!checkUniqueUsername(addUsername.getText(), data)){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Username must be unique.");
                    alert.show();
                    return;
                }
                if (!checkPhoneOnlyDigits(addPhone.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Phone number can contain only digits.");
                    alert.show();
                    return;
                }

                UserFx ufx = new UserFx(
                        addUsername.getText(),
                        "123",
                        addPhone.getText(),
                        addCountry.getText(),
                        addCity.getText(),
                        chAdmin.isSelected(),
                        chBanned.isSelected()
                );
                userManager.create(ufx);
                data.add(ufx);
                addUsername.setText("");
                addPhone.setText("");
                addCity.setText("");
                addCountry.setText("");
                chAdmin.setSelected(false);
                chBanned.setSelected(false);
            }
        });

        delBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                UserFx selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    data.remove(selected);
                    userManager.delete(selected);
                    table.getSelectionModel().clearSelection();
                }
            }
        });

        saveBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (checkAdminNotBanned(data)) {
                    userManager.updateList();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "At least one not banned admin must exist.");
                    alert.show();
                }
            }
        });

        buttonsBox.getChildren().addAll(addBut, delBut, saveBut);
        vbox.getChildren().add(buttonsBox);

    }

    private Boolean checkAdminNotBanned(List<UserFx> users) {
        for (UserFx user : users) {
            if (user.getAdmin() && !user.getBanned()) {
                return true;
            }
        }
        return false;
    }
    
    private Boolean checkUniqueUsername(String username, List<UserFx> users) {
        for (UserFx user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private Boolean checkPhoneOnlyDigits(String phone) {
        return phone.isEmpty() || phone.matches("[0-9]+");
    }
}
