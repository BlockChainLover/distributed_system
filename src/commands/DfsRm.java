package commands;

import java.nio.file.FileSystemException;
import java.util.ArrayList;

import core.MasterNode;
import core.Node;

public class DfsRm extends Command {

	public DfsRm(String args[]) {
		setCommandId("dfs-rm");
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
		ArrayList<Node> nodes;
		nodes = masterNode.getMap().get(getArgs()[0]);
		if (nodes == null)
			System.err.println(getArgs()[0] + " does not exist");
		else {
			try {
				for (Node n : nodes) {

					n.getDirectory().deleteFile(getArgs()[0]);

				}
				masterNode.getMap().remove(getArgs()[0]);
				System.out.println(getArgs()[0] + " removed");
			} catch (FileSystemException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
