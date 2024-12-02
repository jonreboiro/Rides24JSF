package rides.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

public class MainBean {

    public String goToQueryRides() {
        return "QueryRides"; //lortu nahi dugun xhtml fitxategiaren izena
    }

    public String goToCreateRide() {
        return "CreateRide";
    }
}