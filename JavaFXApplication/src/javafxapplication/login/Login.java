package javafxapplication.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafxapplication.JavaFXApplication;
import javafxapplication.models.UserFx;
 
public class Login {
    @FXML private Label errorText;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML protected void processLogin(ActionEvent event) {
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            errorText.setText("Please enter username and password");
            return;
        }
        
        UserFx user = JavaFXApplication.getInstance().userAuth(usernameField.getText(), passwordField.getText());
        if(user == null){
            errorText.setText("Invalid username or password.");
        }else if(user.getBanned()){        
            errorText.setText("You are banned.");
        }
        else{        
            JavaFXApplication.getInstance().userLogging(user);
            JavaFXApplication.getInstance().gotoMain(false);
        }
    }
    
    

}	
