package session;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nameserver.NamingService;
import server.Car;
import server.ICarRentalCompany;
import server.Reservation;
import server.ReservationException;

public class ManagerSession implements IManagerSession {
	
	/**
	 * Refernce of the NamingService, given by the Agency
	 */
	private NamingService nameService;

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
	public int getNumberOfReservations(String type, String carRentalName) throws RemoteException {
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
	public Set<String> getBestCostumers() throws RemoteException {
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

	// Getters & setters
	public NamingService getNameService() {
		return nameService;
	}

	private void setNameService(NamingService nameService) {
		this.nameService = nameService;
	}
	
	

}
