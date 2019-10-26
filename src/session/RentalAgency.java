package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RentalAgency implements IRentalAgency {

	private List<IReservationSession> reservationSessions = new ArrayList<>();
	
	//Singleton managerSession?
	private ManagerSession managerSession = new ManagerSession("manager");
	
	
	
	@Override
	public IReservationSession createNewReservationSession(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IManagerSession createNewManagerSession(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
