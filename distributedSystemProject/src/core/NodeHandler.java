package core;

public class NodeHandler implements Runnable {
	private NodeHandler instance;
	
	private NodeHandler(){
		
	}
	
	public NodeHandler getInstance(){
		if(instance == null)
			instance = new NodeHandler();
		return instance;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
