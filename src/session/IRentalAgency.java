package session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRentalAgency extends Remote {

	IReservationSession createNewReservationSession(String name) throws RemoteException;

	IManagerSession createNewManagerSession(String name) throws RemoteException;

}
