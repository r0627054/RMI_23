package nameserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import server.ICarRentalCompany;

public interface INamingService extends Remote {

	HashMap<String, ICarRentalCompany> getCarRentalCompanies() throws RemoteException;

	ICarRentalCompany getCompany(String name) throws RemoteException;

	void registerCompany(ICarRentalCompany company) throws RemoteException;

	void unregisterCompany(ICarRentalCompany company) throws RemoteException;

	public ArrayList<String> getAllCompanies() throws RemoteException;

}
