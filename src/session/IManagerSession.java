package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import server.CarType;
import server.ICarRentalCompany;

public interface IManagerSession extends Remote {

	//Companies:
	void registerCompany(ICarRentalCompany company) throws RemoteException;

	void unregisterCompany(ICarRentalCompany company) throws RemoteException;

	// Statistics:
	int getNumberOfReservationsByCarType(String type, String carRentalName) throws RemoteException;

	int getNumberOfReservations(String clientName) throws RemoteException;

	Set<String> getBestCostumers() throws RemoteException;

	CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException;

}
