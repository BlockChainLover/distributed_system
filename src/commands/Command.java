package commands;

import java.util.List;

import core.MasterNode;
import core.Node;

public abstract class Command {
	private String commandId;
	
	private String args[];
	
	public abstract void action(MasterNode masterNode);

	public String getCommandId() {
		return commandId;
	}

	protected void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String[] getArgs() {
		return args;
	}

	protected void setArgs(String[] args) {
		this.args = args;
	}
}
