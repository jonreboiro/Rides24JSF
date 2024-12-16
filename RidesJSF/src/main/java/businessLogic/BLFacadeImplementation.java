package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import dataAccess.DataAccess;
import domain.Ride;
import domain.Traveler;
import domain.User;
import domain.Alert;
import domain.Booking;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation implements BLFacade {
	DataAccess dbManager;
	private User currentUser;
	private String chosenDriver;
	
	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager = new DataAccess();
		dbManager.initializeDB();
	}

	public BLFacadeImplementation(DataAccess da) {

		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager = da;
		dbManager.initializeDB();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getDepartCities() {
		List<String> departLocations = dbManager.getDepartCities();
		return departLocations;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getDestinationCities(String from) {
		List<String> targetCities = dbManager.getArrivalCities(from);
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
		return ride;
	};

	/**
	 * {@inheritDoc}
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		List<Ride> rides = dbManager.getRides(from, to, date);
		return rides;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		List<Date> dates = dbManager.getThisMonthDatesWithRides(from, to, date);
		return dates;
	}
	
	public User getUser (String email) {
		return dbManager.getUser(email);
	}

	public void close() {
		DataAccess dB4oManager = new DataAccess();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initializeBD() {
		dbManager.initializeDB();
	}

	@Override
	public void register(User newUser) {
		boolean registered = dbManager.register(newUser);
		if (registered) {
			this.currentUser = newUser;
		}
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public List<String> getDrivers() {
		return this.dbManager.getDrivers();
	}

	@Override
	public void setChosenDriver(String email) {
		this.chosenDriver = email;
		
	}

	@Override
	public String getChosenDriver() {
		return this.chosenDriver;
	}

	@Override
	public List<Ride> getRidesByDriver(String driverEmail) {
		return this.dbManager.getRidesByDriver(driverEmail);
	}

	@Override
	public void bookRide(Booking booking) {
		try {
			this.dbManager.bookRide(booking);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createAlert(Alert newAlert) {
		this.dbManager.createAlert(newAlert);
		
	}

	@Override
	public void updateTraveler(Traveler currentTraveler) {
		this.dbManager.updateTraveler(currentTraveler);
		
	}

}
