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
	private float money;
	
	private static final long serialVersionUID = 1L;
	
	public Traveler(String email, String name, String password) {
		super(email, name, password, "Traveler");
		this.money = 0;
	}
	
	public Traveler(String email, String name, String password, float money) {
		super(email, name, password, "Traveler");
		this.money = money;
	}
	
	public Traveler () {
		super();
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}


}