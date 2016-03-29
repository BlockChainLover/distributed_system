package core;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

import commands.Command;

public class NodeHandler implements Runnable {
	private static NodeHandler instance;

	private List<Node> nodes = new ArrayList<Node>();
	private MasterNode masterNode;

	private NodeHandler() {
		// create master Node
		masterNode = new MasterNode();
	}

	public static NodeHandler getInstance() {
		if (instance == null)
			instance = new NodeHandler();
		return instance;
	}

	/**
	 * Finished
	 * 
	 * @param c
	 * @throws NotDirectoryException
	 */
	public void addNode(Command c) throws NotDirectoryException {
		// check if directory is specified
		String[] args = c.getArgs();
		int id = 0;
		if (args.length > 0) {
			String dir = args[0];
			// check if dir exist
			File f = new File(dir);
			if (f.isDirectory()) {
				// retrieve the id
				dir = dir.replace("node", "");
				try {
					id = Integer.parseInt(dir);
				} catch (NumberFormatException e) {
					System.err.println("Can't create node, the id/directory path is not correct ![" + e.getLocalizedMessage() + "]");
					return;
				}
				if (getNodeById("" + id) != null) {
					System.err.println("Node " + id + " already exist !");
					return;
				}
				Directory directory = new Directory(dir);
				nodes.add(new Node("" + id, directory));
			} else {
				throw new NotDirectoryException("Directory " + dir + " not found !");
			}

		} else {
			// search for the next id
			File root = new File(Core.ROOT);
			for (File f : root.listFiles()) {
				if (f.isDirectory()) {
					String s = f.getName();
					s = s.replace("node", "");
					int fId = Integer.parseInt(s);
					if (id <= fId)
						id = fId + 1;
				}
			}
			Directory dir = new Directory(Core.ROOT + File.separator + "node" + id);
			nodes.add(new Node("" + id, dir));
		}
	}

	public void removeNode(Command c) {
		String[] args = c.getArgs();
		if (args.length == 1) {
			try {
				int id = Integer.parseInt(args[0].replace("node", ""));
				int i = -1;
				for (int j = 0; j < nodes.size(); j++)
					if (nodes.get(j).getId().equals("" + id)) {
						i = j;
						break;
					}
				if (i != -1)
					nodes.get(i).stop();
			} catch (NumberFormatException e) {
				System.err.println("Can't find node with id " + args[0] + " !");
			}
		}else if (args.length == 2) {
			try {
				int id = Integer.parseInt(args[1].replace("node", ""));
				int i = 0;
				for (int j = 0; j < nodes.size(); j++)
					if (nodes.get(j).getId().equals("" + id)) {
						i = j;
						break;
					}
				nodes.get(i).stop();

				if (args.length > 1) {
					switch (args[0]) {
					case "-r":
						System.err.println("Remove the node from the node list !");
						nodes.remove(i);
						break;
					default:
						break;
					}
				}
			} catch (NumberFormatException e) {
				System.err.println("Can't find node with id " + args[1] + " !");
			}
		}else{
			System.err.println("Command not valid !");
		}
	}

	public List<Node> getNodesByLoad() {
		List<Node> ns = new ArrayList<Node>();

		for (Node n : nodes) {
			int i = 0;
			for (i = 0; i < ns.size(); i++) {
				if (n.getBurden() < ns.get(i).getBurden()) {
					break;
				}
			}
			ns.add(i, n);
		}
		
		return ns;
	}

	private void listNode(Command c) {
		List<Node> list = new ArrayList<Node>();
		if (c.getArgs().length > 0) {
			switch (c.getArgs()[0]) {
			case "-l":
				list = getNodesByLoad();
				System.out.println(" --Node-- \t --Size--");
				for (Node n : list) {
					System.out.println(" node" + n.getId() + "\t " + n.getBurden() + " Bytes");
				}
				break;
			default:
				System.err.println("Command [" + c.getCommandId() + " " + c.getArgs()[0] + "] not found !");
			}
		} else {
			list = nodes;
			System.out.println(" --Node-- ");
			for (Node n : list) {
				System.out.println(" node" + n.getId());
			}
		}

	}

	public List<Node> getNodes() {
		return nodes;
	}

	public Node getNodeById(String id) {
		Node n = null;
		for (Node node : nodes) {
			if (node.getId().equals(id)) {
				n = node;
				break;
			}
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
				System.err.println("Can't add node " + e.getLocalizedMessage());
			}
			return;
		case "dfs-removeNode":
			removeNode(c);
			return;
		case "dfs-lsNode":
			listNode(c);
			return;
		default:
			break;
		}

		c.action(masterNode);
	}

}
