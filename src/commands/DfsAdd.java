package commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

import core.Core;
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

	private void addFile(File src, String dest, MasterNode masterNode) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		try {
			List<Node> loadNode = NodeHandler.getInstance().getNodesByLoad();
			long time = System.currentTimeMillis();
			// add file on nodes

			for (int i = 0; i < masterNode.getReplicationRate() && i < loadNode.size(); i++) {
				loadNode.get(i).getDirectory().putFile(src, dest, time);
				nodes.add(loadNode.get(i));
			}

			// if ok add file to masterNode
			String path = dest;
			if (!path.startsWith(File.separator))
				path = File.separator + path;
			masterNode.getMap().put(path, nodes);
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
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Not enough arguments !");
		}
	}

	private void addDirectory(File dir, String destDir, MasterNode masterNode) {
		ArrayList<String> fileList = masterNode.recursivScan(dir);
		if (fileList.isEmpty()) {
			System.out.println(dir.getPath() + " is empty.");
		} else {
			// search if file already in the map
			for (String file : fileList) {
				// need to remove the "id\" in the path
				String s = file.substring(dir.getPath().length());
				s=destDir+s;
				addFile(new File(file), s, masterNode);
			}
		}
	}

	@Override
	public void action(MasterNode masterNode) {
		File file = new File(getArgs()[0]);
		
		if(file.isFile()){
			addFile(file, getArgs()[1], masterNode);
			System.out.println(getArgs()[0] + " added successfully at position " + getArgs()[1]);
		}else if(file.isDirectory()){
			addDirectory(file, getArgs()[1], masterNode);
			System.out.println(getArgs()[0] + " added successfully at position " + getArgs()[1]);
		}else{
			System.out.println();
		}
		

		// if not delete file on nodes
	}

}
