package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterNode {
	private HashMap<String, ArrayList<String>> map;
	
	public MasterNode(){
		
	}
	
	public void refreshMap(){
		List<Node> nodes = NodeHandler.getInstance().getNodes();
	}
}
