package domain;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Entity;

@MappedSuperclass
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String email;
	private String name;
	private String password;
	private String mota;

	public User(String email, String name, String password, String mota) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.mota = mota;
	}

	public User() {
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return email + ";" + name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email != other.getEmail())
			return false;
		return true;
	}
}
