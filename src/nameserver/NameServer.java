package nameserver;

import java.rmi.RemoteException;
import java.util.ArrayList;

import server.ICarRentalCompany;

public class NameServer {

	private static ArrayList<ICarRentalCompany> companies = new ArrayList<ICarRentalCompany>();

	public static ArrayList<ICarRentalCompany> getCarRentalCompanies() {

		// Return a copy to avoid having users changing contents without using functions
		return new ArrayList<ICarRentalCompany>(companies);
	}

	public static ICarRentalCompany getCompany(String name) throws RemoteException {
		for (ICarRentalCompany c : getCarRentalCompanies()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		throw new RemoteException("No company found for name!");
	}

	public static void registerCompany(ICarRentalCompany company) throws RemoteException {
		System.out.println("Company (" + company.getName() + ") added in nameserver!");
		getCarRentalCompanies().add(company);
	}

	public static void unregisterCompany(ICarRentalCompany company) throws RemoteException {
		System.out.println("Company (" + company + ") removed in nameserver!");
		getCarRentalCompanies().remove(company);
	}

}
