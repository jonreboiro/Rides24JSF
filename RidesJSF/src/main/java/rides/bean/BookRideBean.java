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
import domain.Alert;
import domain.Booking;
import domain.Ride;
import domain.Traveler;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class BookRideBean {
	private List<String> arrivalCities;
	private Date data;
	private List<Ride> filteredRides;
	private String selectedDepartCity;
	private String selectedArrivalCity;
	private List<Integer> seatOptions;
	private Ride selectedRide;
	private Integer numSeats;
	public boolean isAukeratuPressed;
	


	private BLFacade businessLogic;

	public BookRideBean() {
		businessLogic = FacadeBean.getBusinessLogic();
		this.arrivalCities = new ArrayList<>();
		filteredRides = new ArrayList<>();
		seatOptions = new ArrayList<>();
		this.isAukeratuPressed = false;

	}


	public List<Ride> getFilteredRides() {
		return filteredRides;
	}

	public void setFilteredRides(List<Ride> filteredRides) {
		this.filteredRides = filteredRides;
	}

	public String getSelectedDepartCity() {
		return selectedDepartCity;
	}

	public void setSelectedDepartCity(String selectedDepartCity) {
		System.out.println(selectedDepartCity + "set select metodoan");
		this.selectedDepartCity = selectedDepartCity;
		// Update arrival cities when the departure city changes
		updateArrivalCities();
	}

	public void updateArrivalCities() {
		if (selectedDepartCity != null && !selectedDepartCity.isEmpty()) {
			arrivalCities = businessLogic.getDestinationCities(selectedDepartCity);
		} else {
			arrivalCities = null;
		}
	}

	public String getSelectedArrivalCity() {
		return selectedArrivalCity;
	}

	public void setSelectedArrivalCity(String selectedArrivalCity) {
		System.out.println(selectedArrivalCity + "set select metodoan");
		this.selectedArrivalCity = selectedArrivalCity;
	}

	public List<String> getDepartCities() {
		return businessLogic.getDepartCities();
	}

	public List<String> getArrivalCities() {
		return arrivalCities;
	}

	public void setArrivalCities(List<String> arrivalCities) {
		this.arrivalCities = arrivalCities;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void onDateSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("" + event.getObject()));
	}

	public void updateRides() {
		if (selectedDepartCity != null && selectedArrivalCity != null && data != null) {
			filteredRides = businessLogic.getRides(selectedDepartCity, selectedArrivalCity, data);
			this.isAukeratuPressed = true;
			if (filteredRides == null || filteredRides.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "No rides found for the selected criteria.", ""));
			}
		} else {
			filteredRides = null;
			this.isAukeratuPressed = false;
		}
	}


	public void setAukeratuPressed(boolean isAukeratuPressed) {
		this.isAukeratuPressed = isAukeratuPressed;
	}

	public void sartuArrivalCities() {
		System.out.println(this.selectedArrivalCity + "listener");
	}
	
	public void onRowSelect(org.primefaces.event.SelectEvent event) {
		selectedRide = (Ride) event.getObject();
		seatOptions.clear();
		if (selectedRide != null) {
			for (int i = 1; i <= selectedRide.getnPlaces(); i++) {
				seatOptions.add(i);
			}
		}
	}
	
	public void bookRide() {
        if (selectedRide == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No ride selected.", ""));
            return;
        }

        if (numSeats == null || numSeats <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a valid number of seats.", ""));
            return;
        }

        try {
            Traveler currentTraveler = (Traveler) businessLogic.getCurrentUser();
            if (currentTraveler == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in as a traveler to book a ride.", ""));
                return;
            }

            double totalPrice = selectedRide.getPrice() * numSeats;

            if (currentTraveler.getMoney() < totalPrice) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insufficient funds to book this ride.", ""));
                return;
            }

            if (selectedRide.getnPlaces() < numSeats) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not enough seats available.", ""));
                return;
            }

            Booking booking = new Booking(selectedRide, currentTraveler, numSeats);

            currentTraveler.setMoney(currentTraveler.getMoney() - totalPrice);
            selectedRide.setnPlaces(selectedRide.getnPlaces() - numSeats); 

            businessLogic.bookRide(booking);
            this.selectedRide = null;

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Ride booked successfully!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred: " + e.getMessage(), ""));
        }
    }
	
	public List<Integer> getSeatOptions() {
		return seatOptions;
	}

	public void setSeatOptions(List<Integer> seatOptions) {
		this.seatOptions = seatOptions;
	}

	public Ride getSelectedRide() {
		return selectedRide;
	}

	public void setSelectedRide(Ride selectedRide) {
		this.selectedRide = selectedRide;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public void createAlert() {
		System.out.println("Meotodoa ez dago implementatuta");
		/**
		 try {
	            Traveler currentTraveler = (Traveler) businessLogic.getCurrentUser();
	            
	            if (currentTraveler == null) {
	                FacesContext.getCurrentInstance().addMessage(null, 
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in as a traveler to create an alert.", ""));
	                return;
	            }
	            
	            if (selectedDepartCity == null || selectedArrivalCity == null || data == null) {
	                FacesContext.getCurrentInstance().addMessage(null, 
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select valid criteria for the alert.", ""));
	                return;
	            }
	            
	            Alert newAlert = new Alert(selectedDepartCity, selectedArrivalCity, data, currentTraveler);
	            
	            businessLogic.createAlert(newAlert);
	            businessLogic.updateTraveler(currentTraveler);
	            	            
	            FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alert created successfully!", ""));
	            
	        } catch (Exception e) {
	            FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating the alert: " + e.getMessage(), ""));
	        }
	        **/
	}
	
	public boolean pressed () {
		return this.isAukeratuPressed;
	}
	public String close() {
		return "Main";
	}
}
