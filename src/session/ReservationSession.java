package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import nameserver.NamingService;
import server.CarType;
import server.ICarRentalCompany;
import server.Quote;
import server.Reservation;
import server.ReservationConstraints;
import server.ReservationException;

/**
 * The implementation of the IReservationSession (Remote interface). A
 * reservetion session contains the quotes of the client and the name of the
 * client. It makes an implementation of all the methods specified in the Remote
 * interface.
 * 
 * @author Dries Janse, Steven Ghekiere
 *
 */
public class ReservationSession implements IReservationSession {

	/**
	 * The (String) name of the client.
	 */
	private String clientName;

	/**
	 * The list of quotes of the client.
	 */
	private List<Quote> quotes;

	/**
	 * Reference to the naming service, given by the Agency
	 */
	private NamingService nameService;

	public ReservationSession(String clientName, NamingService nameService) {
		setClientName(clientName);
		setNameService(nameService);
		setQuotes(new ArrayList<Quote>());
	}

	@Override
	public Quote createQuote(String name, Date start, Date end, String carType, String region)
			throws RemoteException, ReservationException {
		for (String comp : this.getNameService().getAllCompanies()) {
			if (this.getNameService().getCompany(comp)
					.canCreateQuote(new ReservationConstraints(start, end, carType, region), name)) {
				Quote q = this.getNameService().getCompany(comp)
						.createQuote(new ReservationConstraints(start, end, carType, region), name);
				this.addQuote(q);
				return q;
			}
		}
		throw new RemoteException("Cannot create quote");
	}

	@Override
	public List<Quote> getCurrentQuotes() throws RemoteException {
		return getQuotes();
	}

	@Override
	public synchronized List<Reservation> confirmQuotes(String name) throws RemoteException {
		List<Reservation> result = new ArrayList<Reservation>();
		try {
			for (Quote q : getQuotes()) {
				if (q.getCarRenter().equals(name)) {

					ICarRentalCompany companyOfQuote = nameService.getCompany(q.getRentalCompany());
					result.add(companyOfQuote.confirmQuote(q));
				}
			}
		} catch (ReservationException | RemoteException e) {
			System.out.println("Error with confirming quotes! All quotes will be rolled back. NAME= " + name);
			for (Reservation r : result) {
				nameService.getCompany(r.getRentalCompany()).cancelReservation(r);
			}
		}
		return result;
	}

	@Override
	public List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		List<CarType> result = new ArrayList<CarType>();

		for (ICarRentalCompany company : nameService.getCarRentalCompanies().values()) {
			result.addAll(company.getAvailableCarTypes(start, end));
		}

		// removing duplicates
		LinkedHashSet<CarType> resultSet = new LinkedHashSet<>(result);
		List<CarType> resultWithoutDuplicates = new ArrayList<>(resultSet);

		return resultWithoutDuplicates;
	}

	@Override
	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		CarType result = null;
		double currentCheapestPrice = Double.MAX_VALUE;

		for (ICarRentalCompany company : nameService.getCarRentalCompanies().values()) {
			for (CarType carType : company.getAvailableCarTypes(start, end)) {

				if (carType.getRentalPricePerDay() < currentCheapestPrice) {
					result = carType;
					currentCheapestPrice = carType.getRentalPricePerDay();
				}
			}

		}

		if (result == null) {
			throw new RemoteException("No cars found.");
		}

		return result;
	}

	public void addQuote(Quote quote) {
		this.getQuotes().add(quote);
	}

	// Setters & getters
	public String getClientName() {
		return clientName;
	}

	private void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<Quote> getQuotes() {
		return quotes;
	}

	private void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}

	public NamingService getNameService() {
		return nameService;
	}

	private void setNameService(NamingService nameService) {
		this.nameService = nameService;
	}

}
