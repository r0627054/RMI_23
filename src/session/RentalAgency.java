package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import nameserver.NamingService;
import server.ICarRentalCompany;

public class RentalAgency implements IRentalAgency {

	/**
	 * List of client reservation sessions, one session for each 'normal' client.
	 */
	private List<IReservationSession> reservationSessions;
	
	/**
	 * Single manager session, used by the manager client of the agency.
	 */
	private ManagerSession managerSession;

	/**
	 * The naming service that holds the car rental companies.
	 * We give a reference of this service to each session.
	 */
	private NamingService nameService;

	
	public RentalAgency() {
		setNameService(new NamingService());

		setReservationSessions(new ArrayList<IReservationSession>());
		setManagerSession(new ManagerSession(nameService));
	}

	@Override
	public IReservationSession createNewReservationSession(String name) throws RemoteException {
		return new ReservationSession(name, nameService);
	}

	@Override
	public IManagerSession createNewManagerSession(String name) throws RemoteException {
		return managerSession;
	}

	@Override
	public void registerCompany(ICarRentalCompany company) throws RemoteException {
		nameService.registerCompany(company);
		System.out.println(company.getName() + " IS REGISTRED BY AGENCY");
	}

	//Getters & Setters
	public List<IReservationSession> getReservationSessions() {
		return reservationSessions;
	}

	private void setReservationSessions(List<IReservationSession> reservationSessions) {
		this.reservationSessions = reservationSessions;
	}

	public ManagerSession getManagerSession() {
		return managerSession;
	}

	private void setManagerSession(ManagerSession managerSession) {
		this.managerSession = managerSession;
	}

	public NamingService getNameService() {
		return nameService;
	}

	private void setNameService(NamingService nameService) {
		this.nameService = nameService;
	}

	
}
