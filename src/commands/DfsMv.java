package commands;

public class DfsMv extends Command {

	
	public DfsMv(String args[]){
		setCommandId("dfs-mv");
		String arguments[];
		int ind = 0;
		if(args.length > 0)
			if(args[0].equals("dfs-mv")){
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