package commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
		HashMap<String, ArrayList<Node>> map = masterNode.getMap();
		String[] args = this.getArgs();
		String path = "";

		boolean recursiv = false;

		if (args.length == 0) {
			System.err.println("dfs-ls with not args !");
		} else if (args.length == 1) {
			path = args[0];
		} else {
			switch (args[0]) {
			case "-R":
			case "-r":
				recursiv = true;
				path = args[1];
				break;
			default:
				System.err.println(args[0]+" not found as a dfs-ls internal argument.");
				break;
			}
		}
		path += path.endsWith(File.separator) ? "" : File.separator;

		ArrayList<String> dir = new ArrayList<>();

		for (Entry<String, ArrayList<Node>> e : map.entrySet()) {
			String key = e.getKey();
			if (key.startsWith(path)) {
				if (!key.substring(path.length()).contains(File.separator) || recursiv) {
					System.out.println(e.getKey());
				}else{
					// /images/toto.png
					String dirName = key.substring(path.length());
					dirName = dirName.substring(0, dirName.indexOf(File.separator));
					dirName = File.separator+dirName;
					
					if(!dir.contains(dirName)){
						System.out.println(dirName);
						dir.add(dirName);
					}
				}
			}
		}
	}

}
