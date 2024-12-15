package domain;

import javax.persistence.*;


import java.io.Serializable;

@Entity
@TableGenerator(name = "Booking", initialValue = 0, allocationSize = 1)
public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "Booking")
	private int bookNumber;
	@ManyToOne
	private Ride ride;
	@ManyToOne
	private Traveler traveler;
	private int seats;
	private String status;

	public Booking(Ride ride, Traveler traveler, int seats) {
		this.ride = ride;
		this.traveler = traveler;
		this.seats = seats;
		this.status = "NotDefined";
	}
	public Booking() {
		
	}

	public int getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(int bookNumber) {
		this.bookNumber = bookNumber;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double prezioaKalkulatu() {
		return this.ride.getPrice() * this.seats;
	}
}