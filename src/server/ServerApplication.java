package server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import nameserver.NamingServiceApplication;
import session.RentalAgencyApplication;

public class ServerApplication {

	public static void main(String[] args) throws NumberFormatException, ReservationException, IOException, NotBoundException {
		//Start all the server applications
		String[] emptyArguments = {};
		NamingServiceApplication.main(emptyArguments);
		RentalAgencyApplication.main(emptyArguments);
		DockxRentalApplication.main(emptyArguments);
		HertzRentalApplication.main(emptyArguments);
	}

}
