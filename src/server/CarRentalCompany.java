package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarRentalCompany implements ICarRentalCompany {

	private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());

	private List<String> regions;
	private String name;
	private List<Car> cars;
	private Map<String, CarType> carTypes = new HashMap<String, CarType>();

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name, List<String> regions, List<Car> cars) {
		logger.log(Level.INFO, "<{0}> Car Rental Company {0} starting up...", name);
		setName(name);
		this.cars = cars;
		setRegions(regions);
		for (Car car : cars)
			carTypes.put(car.getType().getName(), car.getType());
		logger.log(Level.INFO, this.toString());
	}

	/********
	 * NAME *
	 ********/

	@Override
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	/***********
	 * Regions *
	 **********/
	private void setRegions(List<String> regions) {
		this.regions = regions;
	}

	public List<String> getRegions() {
		return this.regions;
	}

	public boolean operatesInRegion(String region) {
		return this.regions.contains(region);
	}

	/*************
	 * CAR TYPES *
	 *************/

	public Collection<CarType> getAllCarTypes() {
		return carTypes.values();
	}

	public CarType getCarType(String carTypeName) {
		if (carTypes.containsKey(carTypeName))
			return carTypes.get(carTypeName);
		throw new IllegalArgumentException("<" + carTypeName + "> No car type of name " + carTypeName);
	}

	// mark
	public boolean isAvailable(String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[] { name, carTypeName });
		if (carTypes.containsKey(carTypeName)) {
			return getAvailableCarTypes(start, end).contains(carTypes.get(carTypeName));
		} else {
			throw new IllegalArgumentException("<" + carTypeName + "> No car type of name " + carTypeName);
		}
	}

	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
		Set<CarType> availableCarTypes = new HashSet<CarType>();
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				availableCarTypes.add(car.getType());
			}
		}
		return availableCarTypes;
	}

	/*********
	 * CARS *
	 *********/

	private Car getCar(int uid) {
		for (Car car : cars) {
			if (car.getId() == uid)
				return car;
		}
		throw new IllegalArgumentException("<" + name + "> No car with uid " + uid);
	}

	private List<Car> getAvailableCars(String carType, Date start, Date end) {
		List<Car> availableCars = new LinkedList<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType) && car.isAvailable(start, end)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

	@Override
	public List<Car> getAllCars() {
		return new ArrayList<Car>(cars);
	}

	/****************
	 * RESERVATIONS *
	 ****************/

	public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}",
				new Object[] { name, client, constraints.toString() });

		if (!operatesInRegion(constraints.getRegion())
				|| !isAvailable(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate()))
			throw new ReservationException("<" + name + "> No cars available to satisfy the given constraints.");

		CarType type = getCarType(constraints.getCarType());

		double price = calculateRentalPrice(type.getRentalPricePerDay(), constraints.getStartDate(),
				constraints.getEndDate());

		return new Quote(client, constraints.getStartDate(), constraints.getEndDate(), getName(),
				constraints.getCarType(), price);
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
		return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24D));
	}

	public Reservation confirmQuote(Quote quote) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[] { name, quote.toString() });
		List<Car> availableCars = getAvailableCars(quote.getCarType(), quote.getStartDate(), quote.getEndDate());
		if (availableCars.isEmpty())
			throw new ReservationException("Reservation failed, all cars of type " + quote.getCarType()
					+ " are unavailable from " + quote.getStartDate() + " to " + quote.getEndDate());
		Car car = availableCars.get((int) (Math.random() * availableCars.size()));

		Reservation res = new Reservation(quote, car.getId());
		car.addReservation(res);
		return res;
	}

	@Override
	public synchronized void cancelReservation(Reservation res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}", new Object[] { name, res.toString() });
		getCar(res.getCarId()).removeReservation(res);
	}

	@Override
	public String toString() {
		return String.format("<%s> CRC is active in regions %s and serving with %d car types", name,
				listToString(regions), carTypes.size());
	}

	private static String listToString(List<? extends Object> input) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < input.size(); i++) {
			if (i == input.size() - 1) {
				out.append(input.get(i).toString());
			} else {
				out.append(input.get(i).toString() + ", ");
			}
		}
		return out.toString();
	}

	/**
	 * Exercise 3
	 */
	@Override
	public List<Reservation> getReservationsByRenter(String clientName) throws RemoteException {
		List<Reservation> result = new ArrayList<Reservation>();

		for (Car car : this.cars) {
			result.addAll(car.getAllReservationsOfRenter(clientName));
		}

		return result;
	}

	/**
	 * Exercise 4
	 */
	@Override
	public int getNumberOfReservationsForCarType(String carType) throws ReservationException, RemoteException {
		int result = 0;

		CarType type = carTypes.get(carType);
		if (type == null) {
			throw new ReservationException("No car type found for name");
		}

		for (Car car : this.cars) {
			if (car.isOfType(type)) {
				result += car.getAmountOfReservations();
			}
		}

		return result;
	}

	@Override
	public boolean canCreateQuote(ReservationConstraints constraints, String client) throws RemoteException {
		if (!operatesInRegion(constraints.getRegion())
				|| !quoteCanUseCar(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate())) {
			return false;
		}
		return true;
	}

	public boolean quoteCanUseCar(String carTypeName, Date start, Date end) {
		if (carTypes.containsKey(carTypeName)) {
			return getAvailableCarTypes(start, end).contains(carTypes.get(carTypeName));
		} else {
			return false;
		}
	}

	@Override
	public CarType getMostPopularCarTypeIn(int year) throws RemoteException {
		Map<CarType, Integer> amounts = new HashMap<>();

		for (Car car : this.getAllCars()) {
			for (Reservation res : car.getReservations()) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(res.getStartDate());

				if (cal.get(Calendar.YEAR) == year) {
					Integer oldNumberOfPurchases = amounts.get(car.getType());
					Integer newNumberOfPurchases = oldNumberOfPurchases == null ? 1 : ++oldNumberOfPurchases;
					amounts.put(car.getType(), newNumberOfPurchases);
				}
			}
		}

		Map.Entry<CarType, Integer> resultEntry = null;
		for (Map.Entry<CarType, Integer> entry : amounts.entrySet()) {
			if (resultEntry == null || entry.getValue() > resultEntry.getValue()) {
				resultEntry = entry;
			}
		}

		if (resultEntry == null) {
			throw new RemoteException("No CarType found for company & name");
		}

		return resultEntry.getKey();
	}

	@Override
	public Map<String, Integer> getBestCustomer() throws RemoteException {
		Map<String, Integer> customers = new HashMap<String, Integer>();

		for (Car car : this.getAllCars()) {
			for (Reservation res : car.getReservations()) {

				Integer oldNumberOfPurchases = customers.get(res.getCarRenter());
				Integer newNumberOfPurchases = oldNumberOfPurchases == null ? 1 : oldNumberOfPurchases++;
				customers.put(res.getCarRenter(), newNumberOfPurchases);
			}
		}

		return customers;
	}

}