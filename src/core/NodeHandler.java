package core;

import java.util.ArrayList;
import java.util.List;

import commands.Command;

public class NodeHandler implements Runnable {
	private static NodeHandler instance;
	
	private List<Node> nodes = new ArrayList<Node>();
	private List<MasterNode> masterNodes = new ArrayList<MasterNode>();
	
	private NodeHandler(){
		//create master Node
		masterNodes.add(new MasterNode());
	}
	
	public static NodeHandler getInstance(){
		if(instance == null)
			instance = new NodeHandler();
		return instance;
	}

	public void addNode(){
		
	}
	
	public void run() {
		//check if nodes crashed bandicoot
	}

	public void executeCommand(Command c) {
		System.out.println("Execute the command "+c.getCommandId());
	}

	public List<Node> getNodes() {
		return nodes;
	}
}
