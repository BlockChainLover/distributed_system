package commands;

import core.NodeHandler;

public class CommandHandler {
	private static CommandHandler instance;

	private CommandHandler() {

	}

	public static CommandHandler getInstance() {
		if (instance == null)
			instance = new CommandHandler();
		return instance;
	}

	public void processCommand(String s) {
		String[] split = s.split(" ");
		if (split.length > 0) {
			Command c = null;
			switch (split[0]) {
			case "dfs-add":
				c = new DfsAdd(split);
				break;
			case "dfs-get":
				c = new DfsGet(split);
				break;
			case "dfs-rm":
				c = new DfsRm(split);
				break;
			case "dfs-ls":
				c = new DfsLs(split);
				break;
			case "dfs-mv":
				c = new DfsMv(split);
				break;
			case "dfs-cv":
				c = new DfsCp(split);
				break;
			case "dfs-createNode":
				c = new DfsCreateNode(split);
				break;
			case "dfs-removeNode":
				c = new DfsRemoveNode(split);
				break;
			case "dfs-lsNode":
				c = new DfsLsNode(split);
				break;
			case "dfs-help":
			case "help":
			case "--help":
				c = new DfsHelp(split);
				break;
			default:
				break;
			}
			if(c != null){
				NodeHandler.getInstance().executeCommand(c);
			}else{
				System.err.println("Command "+split[0]+" not found !");
			}
		}
	}
}
