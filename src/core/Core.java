package core;

import java.io.File;

import commands.CommandHandler;

public class Core {

	public static final String ROOT = "NodeDir";
	
	public static final boolean DEBUG = true;
	
	public Core(){
		System.out.println("Starting the distributed file system.");
		
		InputListener il = InputListener.getInstance();
		il.start();
		
		NodeHandler nh = NodeHandler.getInstance();
		
		if(DEBUG){
			for(int i = 0; i <= 6; i++)
			CommandHandler.getInstance().processCommand("dfs-createNode node"+i);
		}
		
	}
}
