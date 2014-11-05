import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrinterService implements Printer {
	
	private LinkedList<Pair<Integer,Pair<String,String>>>  jobs = new LinkedList<>();
	private int lastId = 1;
	private volatile Pair<Integer,Pair<String,String>> currentJob = null;
	private AtomicBoolean paused = new AtomicBoolean(true);
	private AtomicBoolean running = new AtomicBoolean(true);
	private HashMap<String, String> parameters = new HashMap<>();
	
	private Thread printCaller = new Thread() {
		
		
		@Override
		public void run(){
			try 
			{
				while(true)
				{
					if (paused.get())
						synchronized (this)  {	this.wait();}
					if (!running.get())
						break;
						
					
					
					synchronized (jobs)
					{
						if (jobs.size() > 0)
							currentJob = jobs.poll();						
					}
					if (currentJob != null)
					{
						//int id = currentJob.getVal1();
						String file = currentJob.getVal2().getVal1();
						String printer = currentJob.getVal2().getVal2();
						System.out.println("Printing "+file+" on "+printer);
						sleep(2000);
						System.out.println("Print completed Succesfully");
						currentJob = null;
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	protected PrinterService() {
		printCaller.start();
	}

	@Override
	public void print(String filename, String printer) {
		synchronized (jobs)
		{
			jobs.add(new Pair<>(lastId, new Pair<>(filename, printer)));
		}
		lastId++;
		
	}

	@Override
	public List<Pair<Integer,Pair<String,String>>> queue() {
		List<Pair<Integer, Pair<String, String>>> list;
		synchronized (jobs)
		{
			list = new ArrayList<>(jobs);
		}
		return list;
	}

	@Override
	public void topQueue(int job) {
		synchronized (jobs)
		{
			Pair<Integer, Pair<String, String>> foundJob = null;
			for(Pair<Integer, Pair<String, String>> jobben : jobs)
			{
				if ( jobben.getVal1() == job )			
					foundJob = jobben;
			}
			if ( foundJob != null )
			{
				jobs.remove(foundJob);
				jobs.addFirst(foundJob);
			}
		}
	}

	@Override
	public void start() {
		paused.set(false);
		synchronized (printCaller)  {printCaller.notify();}
		
		
	}

	@Override
	public void stop() {
		paused.set(true);
		
	}

	@Override
	public void restart() {
		paused.set(true);
		synchronized (jobs)
		{
			jobs.clear();
		}
		
	}

	@Override
	public String status() {
		Pair<Integer,Pair<String,String>> cur = currentJob;
		boolean isPaused = paused.get();
		String runStatus;
		if (isPaused && cur == null)
			runStatus = "Stopping";
		else if (isPaused)
			runStatus = "Stopped";
		else
			runStatus = "Running";
		
		String jobStatus;
		if (cur == null)
			jobStatus = "Idle";
		else
			jobStatus = "Printing "+cur.getVal2().getVal1()+" on "+cur.getVal2().getVal2();
		return "Status: "+runStatus+" -- "+jobStatus;
	}

	@Override
	public String readConfig(String parameter) {
		return parameters.get(parameter);
	}

	@Override
	public void setConfig(String parameter, String value) {
		parameters.put(parameter, value);
		
	}

}
