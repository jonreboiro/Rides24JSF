package dataAccess;

import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.Session;

import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
	private boolean initialized;

	public DataAccess() {
		if (!initialized) {
			initializeDB();
		}
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT COUNT(d) FROM Driver d");
		Long count = (Long) query.uniqueResult();
		session.getTransaction().commit();

		if (count > 0) {
			System.out.println("Database is already initialized. Skipping initialization.");
			this.initialized = true;
		} else {
			this.initialized = false;
		}
		if (!initialized) {
			try {

				Calendar today = Calendar.getInstance();

				int month = today.get(Calendar.MONTH);
				int year = today.get(Calendar.YEAR);
				if (month == 12) {
					month = 1;
					year += 1;
				}

				Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez", "1234");
				Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga", "1234");
				Driver driver3 = new Driver("driver3@gmail.com", "Test driver", "1234");

				driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
				driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
				driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);

				driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

				driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
				driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
				driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

				driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

				session.persist(driver1);
				session.persist(driver2);
				session.persist(driver3);

				session.getTransaction().commit();
				System.out.println("Db initialized");
				this.initialized = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.clear();
		Query query = session.createQuery("SELECT DISTINCT r.fromCity FROM Ride r ORDER BY r.fromCity");
		List<String> cities = query.list();
		session.getTransaction().commit();
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session
				.createQuery("SELECT DISTINCT r.toCity FROM Ride r WHERE r.fromCity=:from ORDER BY r.toCity");
		query.setParameter("from", from);
		List<String> arrivingCities = query.list();
		session.getTransaction().commit();
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			Driver driver = (Driver) session.get(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				session.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			// next instruction can be obviated
			session.persist(driver);
			session.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			session.getTransaction().commit();
			return null;
		}

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<Ride> res = new ArrayList<>();
		Query query = session
				.createQuery("SELECT r FROM Ride r WHERE r.fromCity=:from AND r.toCity=:to AND r.date=:date");
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("date", date);
		List<Ride> rides = query.list();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.fromCity=:from AND r.toCity=:to AND r.date BETWEEN :hasiera and :bukaera");

		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("hasiera", firstDayMonthDate);
		query.setParameter("bukaera", lastDayMonthDate);
		List<Date> dates = query.list();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	public User getUser(String email) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		try {
			Query driverQuery = session.createQuery("FROM Driver d WHERE d.email = :email");
			driverQuery.setParameter("email", email);
			User user = (User) driverQuery.uniqueResult();

			if (user == null) {
				Query travelerQuery = session.createQuery("FROM Traveler t WHERE t.email = :email");
				travelerQuery.setParameter("email", email);
				user = (User) travelerQuery.uniqueResult();
			}

			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	public boolean register(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		try {
			Query driverQuery = session.createQuery("FROM Driver d WHERE d.email = :email");
			driverQuery.setParameter("email", user.getEmail());
			User existingUser = (User) driverQuery.uniqueResult();

			if (existingUser == null) {
				Query travelerQuery = session.createQuery("FROM Traveler t WHERE t.email = :email");
				travelerQuery.setParameter("email", user.getEmail());
				existingUser = (User) travelerQuery.uniqueResult();
			}

			if (existingUser != null) {
				session.getTransaction().rollback();
				return false;
			}

			session.persist(user);
			session.getTransaction().commit();
			System.out.println("User registered successfully: " + user.getEmail());
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	public List<String> getDrivers() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		try {
			Query query = session.createQuery("SELECT d.email FROM Driver d");
			List<String> drivers = query.list();
			session.getTransaction().commit();
			return drivers;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	public List<Ride> getRidesByDriver(String driverEmail) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery(
                "SELECT r FROM Ride r WHERE r.driver.email = :email");
            query.setParameter("email", driverEmail);

            List<Ride> rides = query.list();
            session.getTransaction().commit();

            return rides;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

}
