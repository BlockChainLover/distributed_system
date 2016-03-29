package core;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

import commands.Command;

public class NodeHandler implements Runnable {
	private static NodeHandler instance;

	private List<Node> nodes = new ArrayList<Node>();
	private List<MasterNode> masterNodes = new ArrayList<MasterNode>();

	private NodeHandler() {
		// create master Node
		masterNodes.add(new MasterNode());
	}

	public static NodeHandler getInstance() {
		if (instance == null)
			instance = new NodeHandler();
		return instance;
	}

	public void addNode(Command c) throws NotDirectoryException {
		//check if directory is specified
		String[] args = c.getArgs();
		int id = 0;
		if(args.length>0){
			String dir = args[0];
			//check if dir exist
			File f = new File(dir);
			if(f.isDirectory()){
				//retrieve the id
				dir = dir.replace("node", "");
				try{
					id = Integer.parseInt(dir);
				}catch(NumberFormatException e){
					System.err.println("Can't create node, the id/directory path is not correct !["+e.getLocalizedMessage()+"]");
					return;
				}
				if(getNodeById(""+id) != null){
					System.err.println("Node "+id+" already exist !");
					return;
				}
				Directory directory = new Directory(dir);
				nodes.add(new Node(""+id, directory));
			}else{
				throw new NotDirectoryException("Directory "+dir+" not found !");
			}
			
		}else{
			//search for the next id
			File root = new File(Core.ROOT);
			for(File f : root.listFiles()){
				if(f.isDirectory()){
					String s = f.getName();
					s = s.replace("node", "");
					int fId = Integer.parseInt(s);
					if(id <= fId)
						id = fId + 1;
				}
			}
			Directory dir = new Directory(Core.ROOT+File.separator+"node"+id);
			nodes.add(new Node(""+id, dir));
		}
	}
	
	public Node getNodeById(String id){
		Node n = null;
		for(Node node : nodes){
			if(node.getId().equals(id)){
				n = node;
				break;}
		}
		return n;
	}

	public void run() {
		// check if nodes crashed bandicoot
	}

	public void executeCommand(Command c) {
		System.out.println("Execute the command " + c.getCommandId());
		switch (c.getCommandId()) {
		case "dfs-createNode":
			try {
				addNode(c);
			} catch (NotDirectoryException e) {
				System.err.println("Can't add node "+e.getLocalizedMessage());
			}
			return;
		case "dfs-removeNode":
			return;
		default:
			break;
		}
		
		//TODO
		c.action();
	}

	public List<Node> getNodes() {
		return nodes;
	}
}
