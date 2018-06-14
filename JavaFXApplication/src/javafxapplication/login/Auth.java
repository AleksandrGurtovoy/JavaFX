package javafxapplication.login;
import javafxapplication.managers.GenericDAO;
import javafxapplication.models.UserFx;

public class Auth {
    public static UserFx validate(String username, String password){
        GenericDAO<UserFx> userManager = new GenericDAO(UserFx.class);
        UserFx ufx = userManager.getByField("username", username); 
        if (ufx != null && ufx.getPassword().equals(password))
            return ufx;
        else
            return null;
    }
    
}
