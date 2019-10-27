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
 * The implementation of the IReservationSession (Remote interface).
 * A reservetion session contains the quotes of the client and the name of the client.
 * It makes an implementation of all the methods specified in the Remote interface.
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

	public ReservationSession(String clientName) {
		setClientName(clientName);
		setQuotes(new ArrayList<Quote>());
	}

	@Override
	public void createQuote(String name, Date start, Date end, String carType, String region)
			throws RemoteException, ReservationException {
		NamingService.getCompany(name).createQuote(new ReservationConstraints(start, end, carType, region),
				getClientName());
	}

	@Override
	public List<Quote> getCurrentQuotes() throws RemoteException {
		return getQuotes();
	}

	@Override
	public List<Reservation> confirmQuotes(String name) throws RemoteException {
		List<Reservation> result = new ArrayList<Reservation>();

		try {
			for (Quote q : getQuotes()) {
				if (q.getCarRenter().equals(name)) {

					ICarRentalCompany companyOfQuote = NamingService.getCompany(q.getRentalCompany());
					result.add(companyOfQuote.confirmQuote(q));
				}
			}
		} catch (ReservationException | RemoteException e) {
			System.out.println("Error with confirming quotes! All quotes will be rolled back.");
			for (Reservation r : result) {
				NamingService.getCompany(r.getRentalCompany()).cancelReservation(r);
			}
		}

		return result;
	}

	@Override
	public List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		List<CarType> result = new ArrayList<CarType>();

		for (ICarRentalCompany company : NamingService.getCarRentalCompanies()) {
			result.addAll(company.getAvailableCarTypes(start, end));
		}
		
		//removing dublicates
		LinkedHashSet<CarType> resultSet = new LinkedHashSet<>(result);
		List<CarType> resultWithoutDuplicates = new ArrayList<>(resultSet);

		return resultWithoutDuplicates;
	}

	@Override
	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		CarType result = null;
		double currentCheapestPrice = Double.MAX_VALUE;

		for (ICarRentalCompany company : NamingService.getCarRentalCompanies()) {
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

	// Setters & getters
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<Quote> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}

}
