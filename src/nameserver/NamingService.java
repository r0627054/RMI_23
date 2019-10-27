package nameserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import server.ICarRentalCompany;
/**
 * The naming service is used to locate and register individual car rental companies, based on their names.
 * The companies can run on different servers, independently of the agency.
 * 
 * @author Dries Janse, Steven Ghekiere
 *
 */
public class NamingService {

	/**
	 * HashMap containing all the names and their car rental companies.
	 */
	private static Map<String, ICarRentalCompany> companies = new HashMap<String, ICarRentalCompany>();

	
	/**
	 * Returns a copy of the list of car rental companies
	 * @return a copy of the list of car rental companies
	 */
	public static HashMap<String, ICarRentalCompany> getCarRentalCompanies() {
		// Return a copy to avoid having users changing the array without using functions
		return new HashMap<String, ICarRentalCompany>(companies);
	}

	/**
	 * Returns a car rental company given the name
	 * @param name the name of the car rental company.
	 * @return The car rental company with the given name
	 * @throws RemoteException If there is no car rental company with the given name
	 */
	public static ICarRentalCompany getCompany(String name) throws RemoteException {
		return getCarRentalCompanies().get(name);
	}

	/**
	 * Registers a new car rental company.
	 * @param company the company which will be registered
	 * @throws RemoteException Thrown when the company equals null
	 */
	public static void registerCompany(ICarRentalCompany company) throws RemoteException {
		if(company == null) {
			throw new RemoteException("Cannot register a company which equals null.");
		}
		System.out.println("Company (" + company.getName() + ") is added to nameserver!");
		companies.put(company.getName(), company);
	}

	/**
	 * Unregisters the given car rental company.
	 * @param company the company which will be unregistered.
	 * @throws RemoteException Thrown when the company equals null
	 */
	public static void unregisterCompany(ICarRentalCompany company) throws RemoteException{
		if(company == null) {
			throw new RemoteException("Cannot unregister a company which equals null. ");
		}
		System.out.println("Company (" + company + ") is removed from nameserver!");
		companies.remove(company.getName());
	}

}
