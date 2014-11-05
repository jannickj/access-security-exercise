import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface Printer extends Remote {
	
	void print(String filename, String printer);   // prints file filename on the specified printer
	List<Pair<Integer, Pair<String, String>>> queue();   // lists the print queue on the user's display in lines of the form <job number>   <file name>
	void topQueue(int job) ;   // moves job to the top of the queue
	void start() ;   // starts the print server
	void stop() ;   // stops the print server
	void restart() ;   // stops the print server, clears the print queue and starts the print server again
	String status() ;  // prints status of printer on the user's display
	String readConfig(String parameter) ;   // prints the value of the parameter on the user's display
	void setConfig(String parameter, String value) ;
}
