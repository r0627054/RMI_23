package session;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nameserver.NamingService;
import server.Car;
import server.CarType;
import server.ICarRentalCompany;
import server.Reservation;
import server.ReservationException;

public class ManagerSession implements IManagerSession {

	/**
	 * Name of the user (i.e. manager) using this session
	 */
	private String name;
	
	/**
	 * The name of the rental company managed by this session
	 */
	private String carRentalName;
	
	/**
	 * Reference of the NamingService, given by the Agency
	 */
	private NamingService nameService;

	
	
	public ManagerSession(NamingService nameService, String name, String carRentalName) {
		this.setNameService(nameService);
		this.setName(name);
		this.setCarRentalName(carRentalName);
	}
	
	public ManagerSession(NamingService nameService) {
		setNameService(nameService);
	}

	@Override
	public void registerCompany(ICarRentalCompany company) throws RemoteException {
		nameService.registerCompany(company);
	}

	@Override
	public void unregisterCompany(ICarRentalCompany company) throws RemoteException {
		nameService.unregisterCompany(company);
	}

	@Override
	public int getNumberOfReservationsByCarType(String type, String carRentalName) throws RemoteException {
		try {
			return nameService.getCompany(carRentalName).getNumberOfReservationsForCarType(type);
		} catch (ReservationException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int getNumberOfReservations(String clientName) throws RemoteException {
		int result = 0;

		for (ICarRentalCompany company : nameService.getCarRentalCompanies().values()) {
			result += company.getReservationsByRenter(clientName).size();
		}

		return result;
	}

	@Override
	public Set<String> getBestCustomers() throws RemoteException {
		// key: customer name, value: nbr of purchases
		Map<String, Integer> customers = new HashMap<>();

		// Get all values of amount of purchases for each customer
		for (ICarRentalCompany company : nameService.getCarRentalCompanies().values()) {
			for (Car car : company.getAllCars()) {
				for (Reservation res : car.getReservations()) {

					Integer oldNumberOfPurchases = customers.get(res.getCarRenter());
					Integer newNumberOfPurchases = oldNumberOfPurchases == null ? 1 : oldNumberOfPurchases++;
					customers.put(res.getCarRenter(), newNumberOfPurchases);
				}
			}
		}

		// Get maximum amount of purchases
		int maxAmount = 0;
		for (Map.Entry<String, Integer> entry : customers.entrySet()) {
			if (entry.getValue() > maxAmount) {
				maxAmount = entry.getValue();
			}
		}

		// Fill the result set with correct amount of purchases
		Set<String> result = new HashSet<String>();
		for (Map.Entry<String, Integer> entry : customers.entrySet()) {
			if (entry.getValue() == maxAmount) {
				result.add(entry.getKey());
			}
		}

		return result;
	}

	@Override
	public CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException {
		Map<CarType, Integer> amounts = new HashMap<>();
		ICarRentalCompany company = nameService.getCompany(carRentalCompanyName);

		for (Car car : company.getAllCars()) {
			for (Reservation res : car.getReservations()) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(res.getStartDate());

				if (cal.get(Calendar.YEAR) == year) {
					Integer oldNumberOfPurchases = amounts.get(car.getType());
					Integer newNumberOfPurchases = oldNumberOfPurchases == null ? 1 : oldNumberOfPurchases++;
					amounts.put(car.getType(), newNumberOfPurchases);
				}
			}
		}

		Map.Entry<CarType, Integer> resultEntry = null;

		for (Map.Entry<CarType, Integer> entry : amounts.entrySet()) {
			if (resultEntry == null || entry.getValue() > resultEntry.getValue()) {
				resultEntry = entry;
			}
		}

		if (resultEntry == null) {
			throw new RemoteException("No CarType found for company & name");
		}

		return resultEntry.getKey();
	}

	// Getters & setters
	public NamingService getNameService() {
		return nameService;
	}

	private void setNameService(NamingService nameService) {
		this.nameService = nameService;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getCarRentalName() {
		return carRentalName;
	}

	private void setCarRentalName(String carRentalName) {
		this.carRentalName = carRentalName;
	}
	
}
