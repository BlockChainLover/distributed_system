package commands;

public class DfsRm extends Command {

	public DfsRm(String args[]){
		setCommandId("dfs-rm");
		String arguments[];
		int ind = 0;
		if(args.length > 0)
			if(args[0].equals("dfs-rm")){
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
