package core;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.Random;

public class Node implements Runnable {

	private String id;
	
	private Directory dir;
	
	private Thread t;
	
	private long sleepTime = new Random().nextInt(10000);
	
	public Node(String id) throws NotDirectoryException{
		this.id = id;
		dir = new Directory(Core.ROOT+File.pathSeparator+"node"+id);
		init();
		System.out.println("Node "+id+" created.");
	}
	
	public Node(String id, Directory dir){
		this.id = id;
		this.dir = dir;
		init();
		System.out.println("Node "+id+" created.");
	}
	
	private void init(){
		t = new Thread(this);
		t.start();
	}
	
	public Directory getDirectory(){
		return dir;
	}
	
	public long getBurden() {
		return dir.getBurden();
	}
	
	public String getId(){
		return id;
	}
	
	public boolean isAlive(){
		return t.isAlive();
	}
	
	public void stop(){
		System.err.println("Hard stop the Node "+id);
		t.stop();
	}
	
	public void run() {
		System.out.println("Start the node "+id);
		while(true){
			try {
				t.sleep(sleepTime);
			} catch (InterruptedException e) {
				System.err.println("Node "+id+" crashed ! ["+e.getLocalizedMessage()+"]");
			}
		}
	}


	
	public String toString(){
		return "Node "+id+", size = "+getBurden();
	}
}
