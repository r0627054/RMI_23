package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import server.CarType;
import server.ICarRentalCompany;
import server.ReservationException;

public interface IManagerSession extends Remote {

	//Companies:
	void registerCompany(ICarRentalCompany company) throws RemoteException, ReservationException;

	void unregisterCompany(ICarRentalCompany company) throws RemoteException, ReservationException;

	// Statistics:
	int getNumberOfReservationsByCarType(String type, String carRentalName) throws RemoteException, ReservationException;

	int getNumberOfReservations(String clientName) throws RemoteException, ReservationException;

	Set<String> getBestCustomers() throws RemoteException, ReservationException;

	CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException, ReservationException;

	String getName() throws RemoteException;

	void closeSession() throws RemoteException;
}
