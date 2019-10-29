package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import server.CarType;
import server.Reservation;
import session.IManagerSession;
import session.IRentalAgency;
import session.IReservationSession;

public class Client extends AbstractTestManagement<IReservationSession, IManagerSession> {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

	private IRentalAgency agency;

	/**
	 * The `main` method is used to launch the client application and run the test
	 * script.
	 */
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(null);

		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;

		// creates client with the trips script file
		Client client = new Client("trips", localOrRemote == REMOTE);

		// ---------------------------------------
		System.out.println("CLIENT IS CONNECTED WITH AGENCY\n\n");

		client.run();

		System.out.println("\nCLIENT TRYING TO CLOSE ALL SESSIONS...");
		client.closeAllSessions();
		System.out.println("CLIENT SESSIONS CLOSED.");
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public Client(String scriptFile, boolean remote) {
		super(scriptFile);
		System.out.println("Client scriptfile loaded...");

		try {
			Registry reg;
			if (remote) {
				reg = LocateRegistry.getRegistry("127.0.0.1", 10481);
			} else {
				reg = LocateRegistry.getRegistry();
			}

			if (reg != null) {
				System.out.println("RMI Registry found on client!");
				this.setAgency((IRentalAgency) reg.lookup("rentalAgency"));
			} else {
				throw new ClientException("No registry found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Get Session methods
	@Override
	protected IReservationSession getNewReservationSession(String name) throws Exception {
		System.out.println("New Reservation session is created");
		return this.getAgency().createNewReservationSession(name);
	}

	@Override
	protected IManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {
		System.out.println("New manager session is created");
		return this.getAgency().createNewManagerSession(name, carRentalName);
	}

	// Reservation Session methods
	@Override
	protected void checkForAvailableCarTypes(IReservationSession session, Date start, Date end) throws Exception {
		System.out.println("CLIENT: Check for available car types");
		List<CarType> availableCarTypes = session.getAvailableCarTypes(start, end);
		for (CarType car : availableCarTypes) {
			System.out.println(car);
		}
		System.out.println("\n");
	}

	@Override
	protected void addQuoteToSession(IReservationSession session, String name, Date start, Date end, String carType,
			String region) throws Exception {
		System.out.println("CLIENT creates quote and add to session.");
		session.createQuote(name, start, end, carType, region);
	}

	@Override
	protected List<Reservation> confirmQuotes(IReservationSession session, String name) throws Exception {
		System.out.println("CLIENT confirms all the quotes, NAME = " + name);
		return session.confirmQuotes(name);
	}

	@Override
	protected String getCheapestCarType(IReservationSession session, Date start, Date end, String region)
			throws Exception {
		System.out.println("CLIENT requests the cheapest car type");
		return session.getCheapestCarType(start, end, region).getName();
	}

	// Manager Session methods
	@Override
	protected int getNumberOfReservationsByRenter(IManagerSession ms, String clientName) throws Exception {
		System.out.println("CLIENT requests number of reservations by renter");
		return ms.getNumberOfReservations(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(IManagerSession ms, String carRentalName, String carType)
			throws Exception {
		System.out.println("CLIENT requests number of reservations by cartype");
		return ms.getNumberOfReservationsByCarType(carType, carRentalName);
	}

	@Override
	protected Set<String> getBestClients(IManagerSession ms) throws Exception {
		System.out.println("CLIENT requests all the best clients");
		return ms.getBestCustomers();
	}

	@Override
	protected CarType getMostPopularCarTypeIn(IManagerSession ms, String carRentalCompanyName, int year)
			throws Exception {
		System.out.println("CLIENT requests most popular car type");
		return ms.getMostPopularCarTypeIn(carRentalCompanyName, year);
	}

	private void closeAllSessions() throws RemoteException {
		for (String reservationSessionName : sessions.keySet()) {
			getAgency().closeReservationSession(reservationSessionName);
		}
		getAgency().closeReservationSession(managerResSession.getClientName());
		getAgency().closeManagerSession();
	}

	// Getters & Setters
	public IRentalAgency getAgency() {
		return agency;
	}

	public void setAgency(IRentalAgency agency) throws ClientException {
		if (agency == null) {
			throw new ClientException("The agency cannot be null.");
		}
		this.agency = agency;
	}

}