package rides.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Traveler;
import domain.User;

public class RegisterBean {
	private String email;
	private String name;
	private String password;
	private String confirmPassword;
	private String role;

	private BLFacade businessLogic;

	public RegisterBean() {
		businessLogic = FacadeBean.getBusinessLogic();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String register() {
		if (email == null || email.isEmpty() || name == null || name.isEmpty() || password == null || password.isEmpty()
				|| confirmPassword == null || confirmPassword.isEmpty() || role == null || role.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "All fields are required.", ""));
			return null;
		}

		if (!password.equals(confirmPassword)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match.", ""));
			return null;
		}
		try {

			User existingUser = businessLogic.getUser(email);
			if (existingUser != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is already registered.", ""));
				return null;
			}

			User newUser;
			if ("Driver".equals(role)) {
				newUser = new Driver(email, name, password);
			} else if ("Traveler".equals(role)) {
				newUser = new Traveler(email, name, password);
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid role selected.", ""));
				return null;
			}

			businessLogic.register(newUser);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful.", ""));
			return "Main";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred: " + e.getMessage(), ""));
			return null;
		}
	}

	public String fromRegisterToLogin() {
		return "Login";
	}

	public String back() {
		return "Main";
	}
}
