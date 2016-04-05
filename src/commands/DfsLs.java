package commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import core.MasterNode;
import core.Node;

public class DfsLs extends Command {

	public DfsLs(String args[]) {
		setCommandId("dfs-ls");
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
		Map<String, ArrayList<Node>> map = new TreeMap<>(masterNode.getMap());
		String[] args = this.getArgs();
		String path = "";

		boolean recursiv = false;
		boolean replication = false;

		for (String s : args) {
			switch (s) {
			case "-R":
			case "-r":
				recursiv = true;
				break;
			case "-d":
				replication = true;
				break;
			default:
				path = s;
				break;
			}
		}

		if (path.isEmpty() || !path.endsWith(File.separator)) {
			path += File.separator;
		}
		ArrayList<String> dir = new ArrayList<>();

		for (Entry<String, ArrayList<Node>> e : map.entrySet()) {
			String key = e.getKey();
			boolean printed = false;
			boolean isDir = false;
			if (key.startsWith(path)) {
				if (!key.substring(path.length()).contains(File.separator) || recursiv) {
					System.out.print(e.getKey());
					printed = true;
				} else {
					// /images/toto.png
					String dirName = key.substring(path.length());
					dirName = dirName.substring(0, dirName.indexOf(File.separator));
					dirName = File.separator + dirName;

					if (!dir.contains(dirName)) {
						System.out.print(dirName);
						printed = true;
						isDir = true;
						dir.add(dirName);
					}
				}
				if (replication && printed && !isDir) {
					String nodes = "";
					for(int i = 0; i < e.getValue().size(); i++){
						if(i > 0)
							nodes +=",";
						nodes += ""+e.getValue().get(i).getId();
					}
					System.out.print("\t\t " + e.getValue().size()+"\t\t ["+nodes+"]");
				}

				if (printed)
					System.out.println();
			}
		}
	}

}
