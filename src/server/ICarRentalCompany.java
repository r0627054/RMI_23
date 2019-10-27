package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ICarRentalCompany extends Remote {

	Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;

	Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException, RemoteException;
	
	boolean canCreateQuote(ReservationConstraints constraints, String client) throws RemoteException;

	Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException;

	List<Reservation> getReservationsByRenter(String clientName) throws RemoteException;

	int getNumberOfReservationsForCarType(String carType) throws ReservationException, RemoteException;

	String getName() throws RemoteException;

	void cancelReservation(Reservation reservation) throws RemoteException;

	List<Car> getAllCars() throws RemoteException;

}
