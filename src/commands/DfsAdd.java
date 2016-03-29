package commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

import core.MasterNode;
import core.Node;
import core.NodeHandler;

public class DfsAdd extends Command {

	public DfsAdd(String args[]) {
		setCommandId("dfs-add");
		String arguments[];
		int ind = 0;
		if (args.length > 0)
			if (args[0].equals(getCommandId())) {
				ind = 1;
			}
		arguments = new String[args.length - ind];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = args[i + ind];
		}
		this.setArgs(arguments);
	}

	@Override
	public void action(MasterNode masterNode) {
		List<Node> loadNode = NodeHandler.getInstance().getNodesByLoad();

		ArrayList<Node> nodes = new ArrayList<Node>();
		try {
			// add file on nodes
			for (int i = 0; i < masterNode.getReplicationRate() && i < loadNode.size(); i++) {
				loadNode.get(i).getDirectory().putFile(getArgs()[0], new File(getArgs()[1]));
				nodes.add(loadNode.get(i));
			}

			// if ok add file to masterNode
			masterNode.getMap().put(getArgs()[0], nodes);
			System.out.println("File " + getArgs()[1] + " added successfully at position " + getArgs()[0]);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			// delete files
			// add file on nodes
			for (Node n : nodes) {
				try {
					n.getDirectory().deleteFile(getArgs()[0]);
				} catch (FileSystemException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}
		}catch(IndexOutOfBoundsException e){
			System.err.println("Not enough arguments !");
		}

		// if not delete file on nodes
	}

}
