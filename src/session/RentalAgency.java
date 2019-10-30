package session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import nameserver.INamingService;

public class RentalAgency implements IRentalAgency {

	/**
	 * List of client reservation sessions, one session for each 'normal' client.
	 */
	private Map<String, IReservationSession> reservationSessions;

	/**
	 * List of manager sessions.
	 */
	private Map<String, IManagerSession> managerSessions;

	/**
	 * The naming service that holds the car rental companies. We give a reference
	 * of this service to each session.
	 */
	private INamingService nameService;

	public RentalAgency() {
		setReservationSessions(new HashMap<String, IReservationSession>());
		setManagerSession(new HashMap<String, IManagerSession>());
	}

	@Override
	public IReservationSession createNewReservationSession(String name) throws RemoteException {
		synchronized (reservationSessions) {
			if (getReservationSessions().get(name) != null) {
				return getReservationSessionByName(name);

			} else {
				ReservationSession session = new ReservationSession(name, this.getNameService());
				this.addReservationSession(session);
				return (IReservationSession) UnicastRemoteObject.exportObject(session, 0);
			}
		}
	}

	@Override
	public IManagerSession createNewManagerSession(String name, String carRentalName) throws RemoteException {
		synchronized (managerSessions) {
			if (getManagerSessions().get(name) != null) {
				return getManagerSessionByName(name);

			} else {
				ManagerSession session = new ManagerSession(this.getNameService(), name, carRentalName);
				this.addManagerSession(session);
				return (IManagerSession) UnicastRemoteObject.exportObject(session, 0);
			}
		}
	}

	@Override
	public void closeManagerSession(String sessionName) throws RemoteException {
		synchronized (managerSessions) {
			IManagerSession session = this.getManagerSessionByName(sessionName);

			if (session != null) {
				this.removeManagerSession(session);
				session.closeSession();
			} else {
				throw new RemoteException("ManagerSession does not exist.");
			}

			System.out.println("ManagerSession " + sessionName + " is closed in agency. New amount: #Reserv ="
					+ getReservationSessions().size() + " #Manager = " + getManagerSessions().size());
		}
	}

	@Override
	public void closeReservationSession(String sessionName) throws RemoteException {
		synchronized (reservationSessions) {
			IReservationSession session = this.getReservationSessionByName(sessionName);

			if (session != null) {
				this.removeReservationSession(session);
				session.closeSession();
			} else {
				throw new RemoteException("ReservationSession " + sessionName + " does not exist.");
			}
			System.out.println("ReservationSession " + sessionName + " is closed in agency. New amount: #Reserv ="
					+ +getReservationSessions().size() + " #Manager = " + getManagerSessions().size());
		}
	}

	private void addManagerSession(ManagerSession session) {
		getManagerSessions().remove(session.getName());
	}

	public void addReservationSession(IReservationSession session) throws RemoteException {
		getReservationSessions().put(session.getClientName(), session);
	}

	private void removeManagerSession(IManagerSession session) throws RemoteException {
		getManagerSessions().remove(session.getName());
	}

	public void removeReservationSession(IReservationSession session) throws RemoteException {
		getReservationSessions().remove(session.getClientName());
	}

	public IReservationSession getReservationSessionByName(String name) throws RemoteException {
		for (IReservationSession session : getReservationSessions().values()) {
			if (session.getClientName().equals(name)) {
				return session;
			}
		}
		return null;
	}

	private IManagerSession getManagerSessionByName(String name) throws RemoteException {
		for (IManagerSession session : getManagerSessions().values()) {
			if (session.getName().equals(name)) {
				return session;
			}
		}
		return null;
	}

	// Getters & Setters
	public Map<String, IReservationSession> getReservationSessions() {
		return reservationSessions;
	}

	private void setReservationSessions(HashMap<String, IReservationSession> hashMap) {
		this.reservationSessions = hashMap;
	}

	public Map<String, IManagerSession> getManagerSessions() {
		return managerSessions;
	}

	private void setManagerSession(HashMap<String, IManagerSession> hashMap) {
		this.managerSessions = hashMap;
	}

	public INamingService getNameService() {
		return nameService;
	}

	@Override
	public void setNameService(INamingService nameService) throws RemoteException {
		if (nameService == null)
			throw new RemoteException("Cannot set a null naming service");
		this.nameService = nameService;
	}

}
