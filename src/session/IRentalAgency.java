package session;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.ICarRentalCompany;

public interface IRentalAgency extends Remote {

	IReservationSession createNewReservationSession(String name) throws RemoteException;

	IManagerSession createNewManagerSession(String name, String carRentalName) throws RemoteException;
	
	void closeManagerSession() throws RemoteException;
	
	void closeReservationSession(String sessionName) throws RemoteException;
}
