package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


@Entity
@DiscriminatorValue("TRAVELER")
public class Traveler extends User implements Serializable {
	private double money;
	@OneToMany(mappedBy = "traveler", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Booking> bookedRides = new Vector<Booking>();
	
	private static final long serialVersionUID = 1L;
	
	public Traveler(String email, String name, String password) {
		super(email, name, password, "Traveler");
		this.money = 100;
	}
	
	public Traveler(String email, String name, String password, float money) {
		super(email, name, password, "Traveler");
		this.money = money;
	}
	
	public Traveler () {
		super();
	}
	

	public double getMoney() {
		return money;
	}

	public void setMoney(double d) {
		this.money = d;
	}
	
	public void addBookedRide(Booking bookedRide) {
		bookedRides.add(bookedRide);
		bookedRide.setTraveler(this);
	}

	public List<Booking> getBookedRides() {
		return bookedRides;
	}

	public void setBookedRides(List<Booking> bookedRides) {
		this.bookedRides = bookedRides;
	}


}