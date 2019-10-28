package session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
	 * The naming service that holds the car rental companies. We give a reference
	 * of this service to each session.
	 */
	private NamingService nameService;

	public RentalAgency() {
		setNameService(new NamingService());

		setReservationSessions(new ArrayList<IReservationSession>());
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
	}

	@Override
	public void closeReservationSession(String sessionName) throws RemoteException {
		IReservationSession session = this.getReservationSessionByName(sessionName);

		if (session != null) {
			this.removeReservationSession(session);
		} else {
			throw new RemoteException("ReservationSession " + sessionName + " does not exist.");
		}
	}

	// Getters & Setters
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

	public void addReservationSession(IReservationSession session) {
		reservationSessions.add(session);
	}

	public void removeReservationSession(IReservationSession session) {
		reservationSessions.remove(session);
	}

	public IReservationSession getReservationSessionByName(String name) throws RemoteException {
		for (IReservationSession session : getReservationSessions()) {
			if (session.getClientName().equals(name)) {
				return session;
			}
		}
		return null;
	}

}
