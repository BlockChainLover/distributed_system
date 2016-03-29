package core;

import java.util.ArrayList;
import java.util.List;

import commands.Command;

public class NodeHandler implements Runnable {
	private static NodeHandler instance;
	
	private List<Node> nodes = new ArrayList<Node>();
	private List<MasterNode> masterNodes = new ArrayList<MasterNode>();
	
	private NodeHandler(){
		
	}
	
	public static NodeHandler getInstance(){
		if(instance == null)
			instance = new NodeHandler();
		return instance;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void executeCommand(Command c) {
		
	}
}
