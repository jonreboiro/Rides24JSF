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
	private static final long serialVersionUID = 1L;
	
	public Traveler(String email, String name, String password) {
		super(email, name, password, "Traveler");
	}
	
	public Traveler () {
		super();
	}


}