package commands;

/**
 * dfs-lsNode --> list all runnning nodes
 * dfs-lsNode -l --> list all running nodes with their burden, sort by burden
 * @author nicolas
 *
 */
public class DfsLsNode extends Command {

	public DfsLsNode(String args[]){
		setCommandId("dfs-lsNode");
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
	public void action() {
		// TODO Auto-generated method stub

	}

}
