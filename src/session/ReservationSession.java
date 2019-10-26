package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nameserver.NameServer;
import server.CarType;
import server.ICarRentalCompany;
import server.Quote;
import server.Reservation;
import server.ReservationConstraints;
import server.ReservationException;

public class ReservationSession implements IReservationSession {

	private String clientName;
	private List<Quote> quotes;

	public ReservationSession(String clientName) {
		setClientName(clientName);
		setQuotes(new ArrayList<Quote>());
	}

	@Override
	public void createQuote(String name, Date start, Date end, String carType, String region)
			throws RemoteException, ReservationException {
		NameServer.getCompany(name).createQuote(new ReservationConstraints(start, end, carType, region),
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

					ICarRentalCompany companyOfQuote = NameServer.getCompany(q.getRentalCompany());
					result.add(companyOfQuote.confirmQuote(q));
				}
			}
		} catch (ReservationException | RemoteException e) {
			System.out.println("Error with confirming quotes! All quotes will be rolled back.");
			for (Reservation r : result) {
				NameServer.getCompany(r.getRentalCompany()).cancelReservation(r);
			}
		}

		return result;
	}

	@Override
	public List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		List<CarType> result = new ArrayList<CarType>();

		for (ICarRentalCompany company : NameServer.getCarRentalCompanies()) {
			result.addAll(company.getAvailableCarTypes(start, end));
		}

		return result;
	}

	@Override
	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		CarType result = null;
		double currentCheapestPrice = Double.MAX_VALUE;

		for (ICarRentalCompany company : NameServer.getCarRentalCompanies()) {
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
