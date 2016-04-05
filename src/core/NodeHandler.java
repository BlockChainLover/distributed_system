package core;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import commands.Command;
import commands.CommandHandler;

public class NodeHandler implements Runnable {
	private static NodeHandler instance;

	private List<Node> nodes = new ArrayList<Node>();
	private MasterNode masterNode;

	private Thread thread;

	private HashMap<Node, Integer> disconnedNodes = new HashMap<Node, Integer>();

	private static final Integer MAX_CHECK = 1;

	private static final long NODE_HANDLER_SLEEP_TIME = 5000;

	private NodeHandler() {
		// create master Node
		masterNode = new MasterNode();

		// check if main directory exist
		File dir = new File(Core.ROOT);
		if (!dir.exists()) {
			System.out.println("Root directory not found !");
			dir.mkdirs();
			System.out.println("Create the main directory.");
		}
		thread = new Thread(this);
		thread.start();
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
			File f = new File(Core.ROOT + File.separator + dir);
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
				Directory directory = new Directory(f.getPath());
				Node n = new Node("" + id, directory);
				nodes.add(n);
				masterNode.refreshMap(n);
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
		} else if (args.length == 2) {
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
					case "-R":
					case "-r":
						System.err.println("Remove the node from the node list !");
						// remove the node from the map
						masterNode.removeNode(nodes.get(i));
						nodes.remove(i);
						break;
					default:
						break;
					}
				}
			} catch (NumberFormatException e) {
				System.err.println("Can't find node with id " + args[1] + " !");
			}
		} else {
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
				System.out.println(" -Node-\t-Size- \t\t -Running--");
				for (Node n : list) {
					System.out.println(" node" + n.getId() + "\t " + n.getBurden() + " B \t " + n.isAlive());
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
		while (true) {
			ArrayList<String> commands = new ArrayList<>();
			// check if node crashed
			for (Node n : nodes) {
				try {
					if (!n.isAlive()) {
						System.err.println(n.getId() + "is not alive.");
						Integer check = disconnedNodes.get(n);
						if (check != null) {
							// remove
							if (check >= MAX_CHECK) {
								disconnedNodes.remove(n);
								commands.add("dfs-removeNode -R " + n.getId());
							} else {
								disconnedNodes.put(n, check++);
							}
						} else {
							disconnedNodes.put(n, 1);
						}
					}
				} catch (Exception e) {
					System.err.println("Node " + n.getId() + " doesn't respond !");
				}
			}

			// check node with wes it the disconnectedNodes but cames back
			Iterator it = disconnedNodes.entrySet().iterator();
			while (it.hasNext()) {
				Entry e = (Entry) it.next();
				if (nodes.get(nodes.indexOf(e.getKey())).isAlive()) {
					masterNode.refreshMap((Node)e.getKey());
					// remove from disconnectedNode
					disconnedNodes.remove(e.getKey());
					//need to rescan the node

				}
			}

			for (String s : commands) {
				CommandHandler.getInstance().processCommand(s);
			}

			try {
				thread.sleep(NODE_HANDLER_SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void executeCommand(Command c) {
		//System.out.println("Execute the command " + c.getCommandId());
		switch (c.getCommandId()) {
		case "dfs-createNode":
			try {
				addNode(c);
			} catch (NotDirectoryException e) {
				System.err.println(e.getLocalizedMessage());
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
