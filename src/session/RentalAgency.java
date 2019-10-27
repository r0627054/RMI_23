package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import nameserver.NamingService;
import server.ICarRentalCompany;

public class RentalAgency implements IRentalAgency {

	private List<IReservationSession> reservationSessions = new ArrayList<>();
	
	private NamingService s = new NamingService();
	
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

	@Override
	public void registerCompany(ICarRentalCompany company) throws RemoteException {
		System.out.println("Company komt vbinnen");
		s.registerCompany(company);
		System.out.println(company.getName() + "IS REGISTRED BY AGENCY");
	}
	
	

	
	
}
