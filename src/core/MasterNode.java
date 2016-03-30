package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterNode {
	private HashMap<String, ArrayList<Node>> map;
	
	private int replicationRate = 2;
	
	public MasterNode(){
		map = new HashMap<>();
		System.out.println("Master Node created.");
	}
	
	public HashMap<String, ArrayList<Node>> getMap(){
		return map;
	}
	
	public int getReplicationRate(){
		return replicationRate;
	}
	
	public void refreshMap(){
		System.out.println("Begin nodes scanning.");
		List<Node> nodes = NodeHandler.getInstance().getNodes();
	}
	
	public void refreshMap(Node node){
		System.out.println(" Begin scanning on node : "+node.toString());
		//TODO
		System.err.println("TODO refresh map on a new node (in MasterNode)");
	}
}
