package session;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RentalAgencyApplication {
	
	 public static void main(String[] args) throws RemoteException {
		 System.setSecurityManager(null);
		 RentalAgency agency = new RentalAgency();
		 System.out.println("AGENCY CREATED");
		 
		 IRentalAgency iagency = (IRentalAgency) UnicastRemoteObject.exportObject(agency, 0);
		 
		 Registry registry = LocateRegistry.getRegistry();
		 
		 try {
			registry.rebind("rentalAgency", iagency);
			System.out.println("Rental AGENCY IS REBOUND");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
