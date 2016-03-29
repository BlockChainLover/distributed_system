package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterNode {
	private HashMap<String, ArrayList<String>> map;
	
	public MasterNode(){
		
		System.out.println("Master Node created.");
	}
	
	public void refreshMap(){
		System.out.println("Begin nodes scanning.");
		List<Node> nodes = NodeHandler.getInstance().getNodes();
	}
}
