package server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import session.IManagerSession;
import session.IRentalAgency;

public class HertzRentalApplication extends RentalApplication {
	public static void main(String[] args)
			throws NumberFormatException, ReservationException, IOException, NotBoundException {
		System.setSecurityManager(null);
		
		CrcData data = loadData("hertz.csv");
		CarRentalCompany hertz = new CarRentalCompany(data.name, data.regions, data.cars);
		System.out.println("Hertz Data loaded...");

		ICarRentalCompany iCompany = (ICarRentalCompany) UnicastRemoteObject.exportObject(hertz, 0);

		Registry registry = LocateRegistry.getRegistry();
		IRentalAgency agency = (IRentalAgency) registry.lookup("rentalAgency");
		System.out.println("AGENCY FOUND");
		
		IManagerSession session = agency.createNewManagerSession("Hertz Manager", hertz.getName());
		session.registerCompany(iCompany);
		System.out.println("HERTZ SERVER IS CONNECTED WITH AGENCY");
	}

}
