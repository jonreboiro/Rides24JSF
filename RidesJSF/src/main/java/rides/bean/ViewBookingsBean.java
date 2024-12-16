package rides.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import domain.Booking;
import domain.Traveler;

public class ViewBookingsBean implements Serializable  {

	private static final long serialVersionUID = 1L;

	private List<Booking> bookings;
	
	private BLFacade businessLogic;

	public ViewBookingsBean() {
		businessLogic = FacadeBean.getBusinessLogic();
		this.loadBookings();
	}
	
	@PostConstruct
    public void init() {
        businessLogic = FacadeBean.getBusinessLogic();
        loadBookings();
    }

	public List<Booking> getBookings() {
		return this.bookings;
	}
	
	public void setDriverRides(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	private void loadBookings() {
        try {
            Traveler traveler = (Traveler) businessLogic.getCurrentUser();
            
            if (traveler != null) {
                this.bookings = traveler.getBookedRides();
            } else {
                this.bookings = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bookings = null;
        }
    }
	
	public String close() {
		return "Main";
	}
	
	public void refreshBookings() {
        this.loadBookings();  
    }
	
	
	
}
