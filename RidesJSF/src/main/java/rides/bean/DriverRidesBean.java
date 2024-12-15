package rides.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class DriverRidesBean {
	
	private List<Ride> driverRides;
	
	private BLFacade businessLogic;

	public DriverRidesBean() {
		businessLogic = FacadeBean.getBusinessLogic();
		this.loadDriverRides();
	}

	public List<Ride> getDriverRides() {
		return this.driverRides;
	}
	
	public void setDriverRides(List<Ride> rides) {
		this.driverRides = rides;
	}
	
	private void loadDriverRides() {
        try {
            String driverEmail = businessLogic.getChosenDriver();
            if (driverEmail != null) {
                this.driverRides = businessLogic.getRidesByDriver(driverEmail);
            } else {
                driverRides = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            driverRides = null;
        }
    }
	
	
}
