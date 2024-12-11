package rides.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Traveler;
import domain.User;

public class MainBean {
	
	private BLFacade businessLogic;

    public MainBean() {
        businessLogic = FacadeBean.getBusinessLogic();
    }

    public String goToQueryRides() {
        return "QueryRides";
    }

    public String goToCreateRide() {
        return "CreateRide";
    }
    
    public String goToLogin() {
        return "Login";
    }

    public String goToRegister() {
        return "Register";
    }
    
    public boolean isGuest () {
    	return this.businessLogic.getCurrentUser() == null;
    }
    
    public boolean isDriver() {
    	if (this.businessLogic.getCurrentUser() != null) {
    		return this.businessLogic.getCurrentUser() instanceof Driver;
    	}
        return false;
    }
    
    public boolean isLoggedIn() {
        return this.businessLogic.getCurrentUser() != null;
    }
    
    public String logout() {
        businessLogic.setCurrentUser(null);
        return "refreshMain";
    }

   
}