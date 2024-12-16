package domain;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Entity;

@Entity
public class Alert {
	@Id
	@GeneratedValue
	private Integer alertNumber;
	private String from;
	private String to;
	private Date date;
	@ManyToOne(fetch = FetchType.LAZY)
	private Traveler traveler;
	private boolean found;
	private boolean active;

	public Alert(String from, String to, Date date, Traveler traveler) {
		this.from = from;
		this.to = to;
		this.date = date;
		this.traveler = traveler;
		this.active = true;
	}

	public Integer getAlertNumber() {
		return alertNumber;
	}

	public void setAlertNumber(Integer alertNumber) {
		this.alertNumber = alertNumber;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
