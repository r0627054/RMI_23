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

/**
 * The Remote interface for a ReservationSession. Each car renter obtains their own reservation session
 * that the distributed rental agency will provide
 * @author Dries Janse, Steven Ghekiere
 *
 */
public interface IReservationSession extends Remote {
	
	/**
	 * Creates a new quote with all the given information.
	 * @param name The name of car rental company
	 * @param start The start date
	 * @param end  The end date
	 * @param carType The type of the car
	 * @param region The region where the car needs to be
	 * @throws RemoteException  if things go wrong.
	 * @throws ReservationException  if things go wrong.
	 */
	Quote createQuote(String name, Date start, Date end, String carType, String region)
			throws RemoteException, ReservationException;

	/**
	 * Returns a list of quotes.
	 * @return the list of all pedning quotes
	 * @throws RemoteException
	 */
	List<Quote> getCurrentQuotes() throws RemoteException;

	/**
	 * Confirms all the quotes
	 * @param name the name of the client owning the session
	 * @return the list of all the reservations
	 * @throws RemoteException if things go wrong.
	 */
	List<Reservation> confirmQuotes(String name) throws RemoteException;

	/**
	 * Returns a list of all the available car types between the given dates
	 * @param start The start date
	 * @param end   The end date
	 * @return the list of all the car types between the given dates
	 * @throws RemoteException  if things go wrong.
	 */
	List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;

	/**
	 * Returns the cheapest car type for the given region between the dates.
	 * @param start the start date
	 * @param end  the end date
	 * @param region the region of the car
	 * @return the cheapest car type available between the given dates
	 * @throws RemoteException if things go wrong.
	 */
	CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException;

}
