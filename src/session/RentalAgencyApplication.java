package session;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import nameserver.INamingService;

public class RentalAgencyApplication {
	
	 public static void main(String[] args) throws RemoteException {
		 System.setSecurityManager(null);
		 RentalAgency agency = new RentalAgency();
		 System.out.println("AGENCY CREATED");
		 
		 IRentalAgency iagency = (IRentalAgency) UnicastRemoteObject.exportObject(agency, 0);
		 
		 Registry registry = LocateRegistry.getRegistry();
		 
		 try {
			 //Get the naming service from RMI Registry and set the naming service with the agency
			INamingService nameService = (INamingService) registry.lookup("namingService");
			iagency.setNameService(nameService);
			System.out.println("NAMESERVICE FOUND ON AGENCY APPLICATION");
			
			registry.rebind("rentalAgency", iagency);
			System.out.println("Rental AGENCY IS REBOUND WITH NAMING SERVICE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
