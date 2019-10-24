package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;

import server.CarType;
import server.ReservationConstraints;
import session.IManagerSession;
import session.IRentalSession;

public class Client extends AbstractTestBooking<IRentalSession, IManagerSession> {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

	private final static String NAME1 = "Hertz";
	private final static String NAME2 = "Dockx";

	/**
	 * The `main` method is used to launch the client application and run the test
	 * script.
	 */
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(null);

		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;

		// An example reservation scenario on car rental company 'Hertz' would be...
		Client client = new Client("simpleTrips", NAME1, localOrRemote == 1);
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public Client(String scriptFile, String carRentalCompanyName, boolean remote) {
		super(scriptFile);
		System.out.println("Client scriptfile loaded");

		try {
			Registry reg;
			if (remote) {
				reg = LocateRegistry.getRegistry("127.0.0.1", 10481);
			} else {
				reg = LocateRegistry.getRegistry();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected IRentalSession getNewReservationSession(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void checkForAvailableCarTypes(IRentalSession session, Date start, Date end) throws Exception {
		List<CarType> availableCarTypes = session.getAvailableCarTypes(start, end);

		for (CarType car : availableCarTypes) {
			System.out.println(car);
		}
		System.out.println("\n\n");
	}

	@Override
	protected void addQuoteToSession(IRentalSession session, String name, Date start, Date end, String carType, String region)
			throws Exception {
		session.createQuote(new ReservationConstraints(start, end, carType, region));
		
	}

	@Override
	protected List confirmQuotes(IRentalSession session, String name) throws Exception {
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



}