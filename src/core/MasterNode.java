package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MasterNode implements Runnable {
	private static final long CHECK_TIME = 20000;

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

		if (fileList.isEmpty()) {
			System.out.println("Node" + node.getId() + " is empty.");
		} else {
			// search if file already in the map
			for (String file : fileList) {
				// need to remove the "id\" in the path
				String s = file.substring((Core.ROOT + File.separator + "node" + node.getId()).length());
				if (map.containsKey(s)) {
					// found this file
					System.out.println("File " + s + " already in the map.");
					if (checkFileSimilarities(s, node, map.get(s))) {
						System.out.println("File " + s + " is the same as the files in the map.");
						map.get(s).add(node);
					} else {
						// TODO delete ?
						System.err.println("File " + s + " is not the same file as referenced in the map !");
						
						File f = new File(file);
						if (f.delete()) {
							System.out.println("File " + file + " deleted.");
						} else {
							System.err.println("Can't delete file " + file);
						}
						
					}

				} else {
					ArrayList<Node> n = new ArrayList<Node>();
					n.add(node);
					System.out.println("Add file " + s + " to map");
					map.put(s, n);
				}
			}
		}
	}

	/**
	 * return true if the file is the same as files in the arrayList
	 * 
	 * @param file
	 * @param node
	 * @param arrayList
	 * @return
	 */
	private boolean checkFileSimilarities(String file, Node node, ArrayList<Node> arrayList) {
		// we suposed the file in the arrayList is the base
		File newFile = new File(Core.ROOT+File.separator+"node"+node.getId() + File.separator + file);
		for (Node n : arrayList) {
			File f = new File(Core.ROOT+File.separator+"node"+n.getId() + File.separator + file);
			if (f.lastModified() != newFile.lastModified() || f.length() != newFile.length())
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
		} else {
			System.out.println("Found file " + dir.getPath());
			files.add(dir.getPath());
		}
		return files;
	}

	public void checkMap() {
		int replication = 0;
		for (Entry<String, ArrayList<Node>> e : map.entrySet()) {
			if (e.getValue().size() < replicationRate) {
				System.err.println("File " + e.getKey() + " not enougth replicated.");
				List<Node> loadNode = NodeHandler.getInstance().getNodesByLoad();

				loadNode.removeAll(e.getValue()); // remove the same nodes

				for (int i = e.getValue().size(); i < replicationRate && i < loadNode.size(); i++) {
					try {
						Node n = loadNode.get(i);
						File f = e.getValue().get(0).getFile(e.getKey());
						n.getDirectory().putFile(f, e.getKey(), f.lastModified());
						System.out.println("Created file " + f.getName() + " on node " + n.getId());
						e.getValue().add(n);//add the value t
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				replication++;
			}
		}
		//System.out.println("Check map and found " + replication + " file");
	}

	@Override
	public void run() {

		while (true) {
			try {
				t.sleep(CHECK_TIME);
				checkMap();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void removeNode(Node node) {
		for (Entry<String, ArrayList<Node>> e : map.entrySet()) {
			ArrayList<Node> nodes = e.getValue();
			if(nodes.contains(node)){
				nodes.remove(node);
			}
		}
	}
}
