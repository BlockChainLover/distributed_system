package core;

import java.util.Random;

public class Node implements Runnable {

	private String id;
	
	private String path;
	
	private Thread t;
	
	private long sleepTime = new Random().nextInt(10000);
	
	public Node(String id){
		this.id = id;
	}
	
	public Node(String id, String path){
		this(id);
		this.path = path;
	}
	
	private void init(){
		t = new Thread(this);
		t.start();
	}
	
	public boolean isAlive(){
		return t.isAlive();
	}
	
	public void run() {
		while(true){
			try {
				t.sleep(sleepTime);
			} catch (InterruptedException e) {
				System.err.println("Node "+id+" crashed ! ["+e.getLocalizedMessage()+"]");
			}
		}
	}

}
