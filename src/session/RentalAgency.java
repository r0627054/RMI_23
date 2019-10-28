package session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nameserver.INamingService;
import nameserver.NamingService;
import server.ICarRentalCompany;

public class RentalAgency implements IRentalAgency {

	/**
	 * List of client reservation sessions, one session for each 'normal' client.
	 */
	private Map<String, IReservationSession> reservationSessions;

	/**
	 * Single manager session, used by the manager client of the agency.
	 */
	private ManagerSession managerSession;

	/**
	 * The naming service that holds the car rental companies. We give a reference
	 * of this service to each session.
	 */
	private INamingService nameService;

	public RentalAgency() {
		setReservationSessions(new HashMap<String, IReservationSession>());
		setManagerSession(new ManagerSession(this.getNameService()));
	}

	@Override
	public IReservationSession createNewReservationSession(String name) throws RemoteException {
		ReservationSession session = new ReservationSession(name, this.getNameService());
		this.addReservationSession(session);
		return (IReservationSession) UnicastRemoteObject.exportObject(session, 0);
	}

	@Override
	public IManagerSession createNewManagerSession(String name, String carRentalName) throws RemoteException {
		ManagerSession session = new ManagerSession(this.getNameService(), name, carRentalName);
		this.setManagerSession(session);
		return (IManagerSession) UnicastRemoteObject.exportObject(session, 0);
	}

	@Override
	public void closeManagerSession() throws RemoteException {
		if (this.getManagerSession() == null)
			throw new RemoteException("ManagerSession does not exist.");
		this.setManagerSession(null);
		System.out.println("ManagerSession is closed in agency");
	}

	@Override
	public void closeReservationSession(String sessionName) throws RemoteException {
		IReservationSession session = this.getReservationSessionByName(sessionName);

		if (session != null) {
			this.removeReservationSession(session);

		} else {
			throw new RemoteException("ReservationSession " + sessionName + " does not exist.");
		}
		System.out.println("ReservationSession " + sessionName + " is closed in agency. New amount: "
				+ getReservationSessions().size());
	}

	// Getters & Setters
	public Map<String, IReservationSession> getReservationSessions() {
		return reservationSessions;
	}

	private void setReservationSessions(HashMap<String, IReservationSession> hashMap) {
		this.reservationSessions = hashMap;
	}

	public ManagerSession getManagerSession() {
		return managerSession;
	}

	private void setManagerSession(ManagerSession managerSession) {
		this.managerSession = managerSession;
	}

	public INamingService getNameService() {
		return nameService;
	}

	@Override
	public void setNameService(INamingService nameService) throws RemoteException {
		if (nameService != null) {
			this.nameService = nameService;
		} else {
			throw new RemoteException("Cannot set a null naming service");
		}
	}

	public void addReservationSession(IReservationSession session) throws RemoteException {
		reservationSessions.put(session.getClientName(), session);
	}

	public void removeReservationSession(IReservationSession session) throws RemoteException {
		reservationSessions.remove(session.getClientName());
	}

	public IReservationSession getReservationSessionByName(String name) throws RemoteException {
		for (IReservationSession session : getReservationSessions().values()) {
			if (session.getClientName().equals(name)) {
				return session;
			}
		}
		return null;
	}

}
