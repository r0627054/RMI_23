package session;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nameserver.INamingService;
import server.CarType;
import server.ICarRentalCompany;
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
	private INamingService nameService;

	public ManagerSession(INamingService nameService, String name, String carRentalName) {
		this.setNameService(nameService);
		this.setName(name);
		this.setCarRentalName(carRentalName);
	}

	public ManagerSession(INamingService nameService) {
		setNameService(nameService);
	}

	@Override
	public void registerCompany(ICarRentalCompany company) throws RemoteException {
		getNameService().registerCompany(company);
	}

	@Override
	public void unregisterCompany(ICarRentalCompany company) throws RemoteException {
		getNameService().unregisterCompany(company);
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
		for (ICarRentalCompany company : getNameService().getCarRentalCompanies().values()) {
			
			//Get best customers for each company
			Map<String, Integer> customersTemp = company.getBestCustomer();

			//Check for each entry if it existed in customers map.
			//If not existed, put new key/value pair.
			//If exitsted, put same key with added values.
			for (Map.Entry<String, Integer> entry : customersTemp.entrySet()) {
				
				if (customers.get(entry.getKey()) == null) {
					customers.put(entry.getKey(), entry.getValue());
				} else {
					customers.put(entry.getKey(), entry.getValue() + customers.get(entry.getKey()));
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
		ICarRentalCompany company = getNameService().getCompany(carRentalCompanyName);
		return company.getMostPopularCarTypeIn(year);
	}

	// Getters & setters
	public INamingService getNameService() {
		return nameService;
	}

	private void setNameService(INamingService nameService) {
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
