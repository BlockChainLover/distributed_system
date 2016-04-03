package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterNode implements Runnable{
	private HashMap<String, ArrayList<Node>> map;

	private int replicationRate = 2;

	private Thread t;
	
	public MasterNode() {
		map = new HashMap<>();
		System.out.println("Master Node created.");
		t = new Thread(this);
		t.start();
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
		File dir = node.getDirectory().getRoot();

		ArrayList<String> fileList = recursivScan(dir);
		
		if(fileList.isEmpty()){
			System.out.println("Node"+node.getId()+" is empty.");
		}else{
			//search if file already in the map
			for(String file : fileList){
				//need to remove the "id\" in the path
				String s = file.substring((Core.ROOT+File.separator+"node"+node.getId()).length());
				if( map.containsKey(s)){
					//found this file
					System.out.println("File "+s+" already in the map.");
					if(checkFileSimilarities(s, node, map.get(s))){
						System.out.println("File "+s+" is the same as the files in the map.");
						map.get(s).add(node);
					}else{
						//TODO delete ?
						System.err.println("File "+s+" is not the same as files in the map !");
					}
					
				}else{
					ArrayList<Node> n = new ArrayList<Node>();
					n.add(node);
					System.out.println("Add file "+s+" to map");
					map.put(s, n);
				}
			}
		}
	}

	/**
	 *  return true if the file is the same as files in the arrayList
	 * @param file
	 * @param node
	 * @param arrayList
	 * @return
	 */
	private boolean checkFileSimilarities(String file, Node node, ArrayList<Node> arrayList) {
		//we suposed the file in the arrayList is the base
		File newFile = new File(node.getId()+File.separator+file);
		
		for(Node n : arrayList){
			File f = new File(n.getId()+File.separator+file);
			if(f.lastModified() == newFile.lastModified() && f.length() == newFile.length())
				return false;
		}
		
		return true;
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
