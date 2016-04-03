package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import core.MasterNode;
import core.Node;

public class DfsLs extends Command {

	public DfsLs(String args[]){
		setCommandId("dfs-ls");
		String arguments[];
		int ind = 0;
		if(args.length > 0)
			if(args[0].equals(getCommandId())){
				ind = 1;
			}
		arguments = new String[args.length-ind];
		for(int i = 0 ; i < arguments.length ; i++){
			arguments[i] = args[i+ind];
		}
		this.setArgs(arguments);
	}
	
	@Override
	public void action(MasterNode masterNode) {
		System.err.println("TODO finish the dfs-ls : actualy not working !");
		HashMap <String, ArrayList<Node>> map = masterNode.getMap();
		for(Entry<String,ArrayList<Node>> e : map.entrySet()){
			if(e.getKey().startsWith("\\")){
				System.out.println(e.getKey());
			}
		}
	}

}
