package server;

public class Reservation extends Quote {

	private int carId;

	/***************
	 * CONSTRUCTOR *
	 ***************/

	Reservation(Quote quote, int carId) {
		super(quote.getCarRenter(), quote.getStartDate(), quote.getEndDate(), quote.getRentalCompany(),
				quote.getCarType(), quote.getRentalPrice());
		this.carId = carId;
	}

	/******
	 * ID *
	 ******/

	public int getCarId() {
		return carId;
	}

	/*************
	 * TO STRING *
	 *************/

	@Override
	public String toString() {
		return String.format("Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f",
				getCarRenter(), getStartDate(), getEndDate(), getRentalCompany(), getCarType(), getCarId(),
				getRentalPrice());
	}

	public String getReservationInfo() {
		return String.format("Reservation from %s to %s Car type: %s Car: %s Total price: %.2f",
				getStartDate(), getEndDate(), getCarType(), getCarId(), getRentalPrice());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + carId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		Reservation other = (Reservation) obj;
		if (carId != other.carId)
			return false;
		return true;
	}

	public boolean isRenter(String clientName) {
		return getCarRenter().equals(clientName);
	}
}