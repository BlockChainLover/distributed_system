package core;

import java.io.File;

public class Core {

	public static final String ROOT = "NodeDir";
	
	public Core(){
		System.out.println("Starting the distributed file system.");
		
		InputListener il = InputListener.getInstance();
		il.start();
		
		NodeHandler nh = NodeHandler.getInstance();
		
	}
}
