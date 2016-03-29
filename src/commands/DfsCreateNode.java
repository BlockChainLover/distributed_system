package commands;

import core.MasterNode;

/**
 * dfs-createNode --> create a new node
 * dfs-createNode DIR_PATH --> create a new node and link it to the specified dir
 * @author nicolas
 *
 */
public class DfsCreateNode extends Command {

	public DfsCreateNode(String args[]){
		setCommandId("dfs-createNode");
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
