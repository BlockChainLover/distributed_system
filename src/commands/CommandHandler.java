package commands;

public class CommandHandler implements Runnable {
	private CommandHandler instance;
	
	private CommandHandler(){
		
	}
	
	public CommandHandler getInstance(){
		if(instance == null)
			instance = new CommandHandler();
		return instance;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
}
