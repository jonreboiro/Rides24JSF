package rides.bean;

import java.util.List;

import businessLogic.BLFacade;
import domain.Booking;
import domain.Traveler;

public class ViewBookingsBean {
	
	private List<Booking> bookings;
	
	private BLFacade businessLogic;

	public ViewBookingsBean() {
		businessLogic = FacadeBean.getBusinessLogic();
		this.loadBookings();
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
	
	
}
