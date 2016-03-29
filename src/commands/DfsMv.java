package commands;

import core.MasterNode;

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
		// TODO Auto-generated method stub
		
	}

}
