package session;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import server.CarType;
import server.Quote;
import server.Reservation;
import server.ReservationConstraints;
import server.ReservationException;

public class RentalSession implements IRentalSession {

	private String clientName;
	
	public RentalSession(String clientName) {
		setClientName(clientName);
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public Quote createQuote(ReservationConstraints constraints) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Quote> getCurrentQuotes() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> confirmQuotes(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
