package server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import session.IRentalAgency;

public class DockxRentalApplication extends RentalApplication {
	public static void main(String[] args)
			throws NumberFormatException, ReservationException, IOException, NotBoundException {
		System.setSecurityManager(null);
		
		CrcData data = loadData("dockx.csv");
		CarRentalCompany dockx = new CarRentalCompany(data.name, data.regions, data.cars);
		System.out.println("Dockx Data loaded...");

		ICarRentalCompany iCompany = (ICarRentalCompany) UnicastRemoteObject.exportObject(dockx, 0);

		Registry registry = LocateRegistry.getRegistry();
		IRentalAgency agency = (IRentalAgency) registry.lookup("rentalAgency");
		System.out.println("AGENCY FOUND");
		
		agency.registerCompany(iCompany);
		System.out.println("DOCKX SERVER IS CONNECTED WITH AGENCY");
	}
}
