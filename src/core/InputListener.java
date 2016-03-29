package core;

import java.util.Scanner;

import commands.CommandHandler;

/**
 * Singleton
 * @author nicolas
 *
 */
public class InputListener implements Runnable{
	
	private Scanner sc;
	
	private static InputListener inputListener = null;
	
	private Thread thread;
	
	private InputListener(){
		sc = new Scanner(System.in);
		
		thread = new Thread(this);
	}
	
	public static InputListener getInstance(){
		if(inputListener == null){
			inputListener = new InputListener();
		}
		return inputListener;
	}

	public void run() {
		while(true){
			String s = sc.nextLine();
			CommandHandler.getInstance().processCommand(s);
		}
	}
	
	
}
