package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface IManagerSession extends Remote {

	void registerCompany(String companyName) throws RemoteException;

	void unregisterCompany(String companyName) throws RemoteException;
	
	//Statistics:
	
    int getNumberOfReservations(String type, String carRentalName) throws RemoteException;
    
    int getNumberOfReservations(String clientName) throws RemoteException;

    Set<String> getBestCostumers() throws RemoteException;

}
