package commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

import core.MasterNode;
import core.Node;
import core.NodeHandler;

public class DfsMv extends Command {

	
	public DfsMv(String args[]){
		setCommandId("dfs-mv");
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
			System.err.println(getArgs()[0] + "does not exist");
		else{
			int nSuccess = 0;
			for(Node n : nodes){
				try {
					n.getDirectory().moveFile(getArgs()[0], getArgs()[1]);
					nSuccess++;
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
			if(nSuccess > 0){
				masterNode.getMap().remove(getArgs()[0]);
				masterNode.getMap().put(getArgs()[1], nodes);
			}
		}		
	}
}
