package server;

import java.io.IOException;
import java.rmi.NotBoundException;

import nameserver.NamingServiceApplication;
import session.RentalAgencyApplication;

public class ServerApplication {

	public static void main(String[] args) throws NumberFormatException, ReservationException, IOException, NotBoundException {
		//Start all the server applications
		String[] emptyArguments = {};
		//System.setProperty("java.rmi.server.hostname","192.168.99.1");
		NamingServiceApplication.main(emptyArguments);
		RentalAgencyApplication.main(emptyArguments);
		DockxRentalApplication.main(emptyArguments);
		HertzRentalApplication.main(emptyArguments);
	}

}
