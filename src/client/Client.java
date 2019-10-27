package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import server.CarType;
import server.Reservation;
import session.IManagerSession;
import session.IReservationSession;
import session.RentalAgency;

public class Client extends AbstractTestManagement<IReservationSession, IManagerSession> {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

	//TODO : in client constructor: get registry.lookup("agency") en set deze
	private RentalAgency agency;

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
		Client client = new Client("trips",  localOrRemote == 1);
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public Client(String scriptFile, boolean remote) {
		super(scriptFile);
		System.out.println("Client scriptfile loaded");
		try {
			Registry reg;
			if (remote) {
				reg = LocateRegistry.getRegistry("127.0.0.1", 10481);
			} else {
				reg = LocateRegistry.getRegistry();
			}
			//TODO Hier willen we RentalAgency opvragen/aanmaken

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected IReservationSession getNewReservationSession(String name) throws Exception {
		return agency.createNewReservationSession(name);
	}

	@Override
	protected IManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {
//		return agency.createNewManagerSession(carRentalName);
		//Todo: params?
		return null;
	}

	@Override
	protected void checkForAvailableCarTypes(IReservationSession session, Date start, Date end) throws Exception {
		List<CarType> availableCarTypes = session.getAvailableCarTypes(start, end);

		for (CarType car : availableCarTypes) {
			System.out.println(car);
		}
		System.out.println("\n\n");
	}

	@Override
	protected void addQuoteToSession(IReservationSession session, String name, Date start, Date end, String carType, String region)
			throws Exception {
		session.createQuote(name, start, end, carType, region);
	}

	@Override
	protected List<Reservation> confirmQuotes(IReservationSession session, String name) throws Exception {
		return session.confirmQuotes(name);
	}

	@Override
	protected int getNumberOfReservationsByRenter(IManagerSession ms, String clientName) throws Exception {
		return ms.getNumberOfReservations(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(IManagerSession ms, String carRentalName, String carType) throws Exception {
		return ms.getNumberOfReservations(carType, carRentalName);
	}

	@Override
	protected Set<String> getBestClients(IManagerSession ms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCheapestCarType(IReservationSession session, Date start, Date end, String region)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CarType getMostPopularCarTypeIn(IManagerSession ms, String carRentalCompanyName, int year)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public RentalAgency getAgency() {
		return agency;
	}

	public void setAgency(RentalAgency agency) throws ClientException {
		if(agency == null) {
			throw new ClientException("The agency cannot be null.");
		}
		this.agency = agency;
	}

	
	


}