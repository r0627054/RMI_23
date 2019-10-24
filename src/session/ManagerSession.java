package session;

import java.rmi.RemoteException;
import java.util.Set;

public class ManagerSession implements IManagerSession{

	//Namingservice?
	
	public ManagerSession() {
	}
	
	
	@Override
	public void registerCompany(String companyName) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterCompany(String companyName) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfReservations(String type, String carRentalName) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfReservations(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getBestCostumers() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
