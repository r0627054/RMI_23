package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import server.CarType;
import server.Quote;
import server.Reservation;
import server.ReservationConstraints;
import server.ReservationException;

public interface IReservationSession extends Remote {
	void createQuote(String name, Date start, Date end, String carType, String region)
			throws RemoteException, ReservationException;

	List<Quote> getCurrentQuotes() throws RemoteException;

	List<Reservation> confirmQuotes(String name) throws RemoteException;

	List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;

	CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException;

}
