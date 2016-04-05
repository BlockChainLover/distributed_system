package commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import core.MasterNode;
import core.Node;

public class DfsGet extends Command {

	public DfsGet(String args[]){
		setCommandId("dfs-get");
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
		ArrayList<Node> nodes ;
		nodes = masterNode.getMap().get(getArgs()[0]);
		if(nodes == null)
			System.err.println(getArgs()[0] + " does not exist");
		else{
			for(Node n : nodes){
				try {
					n.getDirectory().retrieveFile(getArgs()[0], new File(getArgs()[1]));
					System.out.println(getArgs()[0] + " retrieved to " + getArgs()[1]);
					break;
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
}