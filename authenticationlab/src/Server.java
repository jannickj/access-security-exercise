import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;


public class Server {

	public static void main(String[] args) throws NotBoundException, AlreadyBoundException, SQLException, IOException
	{
		int port = 1099;
		String db = "localhost:3306";
		String logfile = "message.log";
		
		if (args.length >= 1)
			port = new Integer(args[0]);
		if (args.length >= 2)
			db = args[1];
		if (args.length >= 3)
			logfile = args[2];
		final FileWriter logFile = new FileWriter(logfile);
		
		PrinterLogger logger = new PrinterLogger() {
			
			@Override
			public void Write(String text) {
				try {
					logFile.write(text+"\n");
					logFile.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
		
		Registry reg = LocateRegistry.createRegistry(port);
		
		
		
		try {
		    System.out.println("Loading mysql driver...");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver mysql loaded!");
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		Connection connection = DriverManager.getConnection("jdbc:mysql://"+db+"/printer_authentication?noAccessToProcedureBodies=true", "printer_server", "password");
		MySqlConnector mysqlConn = new MySqlConnector(connection);
		
		AuthenticatedReceiverPrinter authenticatedService = new AuthenticatedReceiverPrinter(mysqlConn,mysqlConn,new PrinterService(), logger);
		
		reg.rebind("Printer", authenticatedService);
		
	}
}
