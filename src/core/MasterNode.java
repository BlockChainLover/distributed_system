package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterNode {
	private HashMap<String, ArrayList<Node>> map;

	private int replicationRate = 2;

	public MasterNode() {
		map = new HashMap<>();
		System.out.println("Master Node created.");
	}

	public HashMap<String, ArrayList<Node>> getMap() {
		return map;
	}

	public int getReplicationRate() {
		return replicationRate;
	}

	public void refreshMap() {
		System.out.println("Begin nodes scanning.");
		List<Node> nodes = NodeHandler.getInstance().getNodes();
	}

	public void refreshMap(Node node) {
		System.out.println(" Begin scanning on node : " + node.toString());
		// TODO
		System.err.println("TODO refresh map on a new node (in MasterNode)");

		File dir = node.getDirectory().getRoot();

		ArrayList<String> fileList = recursivScan(dir);
		
		if(fileList.isEmpty()){
			System.out.println("Node"+node.getId()+" is empty.");
		}else{
			//search if file already in the map
			for(String file : fileList){
				if( map.containsKey(file)){
					//found this file
					//TODO check if the file is the same 
					
					map.get(file).add(node);
				}else{
					ArrayList<Node> n = new ArrayList<Node>();
					n.add(node);
					map.put(file, n);
				}
			}
		}
	}

	public ArrayList<String> recursivScan(File dir) {
		ArrayList<String> files = new ArrayList<>();
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (File child : children) {
				files.addAll(recursivScan(child));
			}
		}else{
			System.out.println("Found file "+dir.getPath());
			files.add(dir.getPath());
		}
		return files;
	}
}
