package commands;

import java.util.List;

import core.MasterNode;
import core.Node;

/**
 * dfs-removeNode NODE_ID --> stop the node wiitch id = NODE_ID
 * dfs-removeNode -r NODE_ID --> stop the node witch id = NODE_ID and remove it from node list
 * @author nicolas
 *
 */
public class DfsRemoveNode extends Command {
	
	
	public DfsRemoveNode(String args[]){
		setCommandId("dfs-removeNode");
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
		// TODO Auto-generated method stub
		
	}

}
