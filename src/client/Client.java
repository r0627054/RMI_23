package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import server.CarType;
import server.ICarRentalCompany;

public class Client extends AbstractTestBooking<Object, Object> {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

	private final static String NAME = "Hertz";
	private ICarRentalCompany icrc = null;

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
		Client client = new Client("simpleTrips", NAME, localOrRemote == 1);
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
			this.icrc = (ICarRentalCompany) reg.lookup(carRentalCompanyName);

			System.out.println("ICarRentalCompany located in rmi registry! = " + this.icrc);

		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected Object getNewReservationSession(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getNewManagerSession(String name, String carRentalName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Check which car types are available in the given period (across all companies
	 * and regions) and print this list of car types.
	 *
	 * @param start start time of the period
	 * @param end   end time of the period
	 * @param session session
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
	protected void checkForAvailableCarTypes(Object session, Date start, Date end) throws Exception {
		Set<CarType> availableCarTypes = icrc.getAvailableCarTypes(start, end);

		for (CarType car : availableCarTypes) {
			System.out.println(car);
		}
		System.out.println("\n\n");
	}

	@Override
	protected void addQuoteToSession(Object session, String name, Date start, Date end, String carType, String region)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List confirmQuotes(Object session, String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getNumberOfReservationsByRenter(Object ms, String clientName) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNumberOfReservationsForCarType(Object ms, String carRentalName, String carType) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}



}