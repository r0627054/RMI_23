package nameserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import session.IRentalAgency;
import session.RentalAgency;

public class NamingServiceApplication {

	public static void main(String[] args) throws RemoteException {
		 System.setSecurityManager(null);
		 NamingService service = new NamingService();
		 System.out.println("NAMESERVICE CREATED");
		 
		 INamingService iNaming = (INamingService) UnicastRemoteObject.exportObject(service, 0);
		 
		 Registry registry = LocateRegistry.getRegistry();
		 
		 try {
			registry.rebind("namingService", iNaming);
			System.out.println("NAMING SERVICE IS REBOUND");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
