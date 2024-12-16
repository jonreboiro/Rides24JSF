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

public class ShowAllRidesBean {
	private String selectedDriver;
	private List<String> drivers;
	
	private BLFacade businessLogic;

	public ShowAllRidesBean() {
		businessLogic = FacadeBean.getBusinessLogic();
		this.drivers = new ArrayList<>();
	}
	
	public List<String> getDrivers() {
		return businessLogic.getDrivers();
	}
	
	public void chooseDriver()  {
		this.businessLogic.setChosenDriver(this.selectedDriver);
	}
	public String getSelectedDriver() {
		return selectedDriver;
	}
	public void setSelectedDriver(String selectedDriver) {
		this.selectedDriver = selectedDriver;
	}
	public void setDrivers(List<String> drivers) {
		this.drivers = drivers;
	}
	
	public String showRides() {
		this.businessLogic.setChosenDriver(this.selectedDriver);
		return "driversRides";
	}
	
	public String close() {
		return "Main";
	}
	
}
