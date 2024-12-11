package rides.bean;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Traveler;
import domain.User;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private User loggedInUser;

	private BLFacade businessLogic;

	public LoginBean() {
		businessLogic = FacadeBean.getBusinessLogic(); // Get business logic reference
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {
        try {
            loggedInUser = businessLogic.getUser(email);

            if (loggedInUser != null && loggedInUser.getPassword().equals(password)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login successful!"));
                this.businessLogic.setCurrentUser(loggedInUser);
                return "fromLoginToMain";
                
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password.", ""));
                return null;  
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error during login: " + e.getMessage(), ""));
            return null;
        }
    }

	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	public String fromLoginToRegister () {
		return "Register";
	}

}
