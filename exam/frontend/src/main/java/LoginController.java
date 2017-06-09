import ejb.UserEJB;
import entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable{

    @EJB
    private UserEJB userEJB;

    private String formUserName;
    private String formPassword;
    private String formConfirmPassword;
    private String formFirstName;
    private String formLastName;

    private String registeredUser;

    public LoginController(){}

    public boolean isLoggedIn(){
        return registeredUser != null;
    }

    public String getRegisteredUser(){
        return registeredUser;
    }

    public User getUser(){
        return userEJB.getUser(registeredUser);
    }

    public String logOut(){
        registeredUser = null;
        return "home.jsf";
    }

    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            registeredUser = formUserName;
            return "home.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){
        if(! formPassword.equals(formConfirmPassword)){
            return "createUser.jsf";
        }

        boolean registered = false;

        try {
            registered = userEJB.createUser(formUserName, formPassword, formFirstName, formLastName);
        } catch (Exception e){}

        if(registered){
            registeredUser = formUserName;
            return "home.jsf";
        } else {
            return "createUser.jsf";
        }
    }

    public String getFormUsername() {
        return formUserName;
    }

    public void setFormUsername(String formUsername) {
        this.formUserName = formUsername;
    }

    public String getFormFirstName() {
        return formFirstName;
    }

    public void setFormFirstName(String formFirstName) {
        this.formFirstName = formFirstName;
    }

    public String getFormLastName() {
        return formLastName;
    }

    public void setFormLastName(String formLastName) {
        this.formLastName = formLastName;
    }

    public String getFormPassword() {
        return formPassword;
    }

    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }

    public String getFormConfirmPassword() {
        return formConfirmPassword;
    }

    public void setFormConfirmPassword(String formConfirmPassword) {
        this.formConfirmPassword = formConfirmPassword;
    }

    public void setRegisteredUser(String registeredUser) {
        this.registeredUser = registeredUser;
    }
}
