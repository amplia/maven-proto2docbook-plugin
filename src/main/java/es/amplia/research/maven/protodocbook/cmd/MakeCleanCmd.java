package es.amplia.research.maven.protodocbook.cmd;

import java.io.IOException;

public class MakeCleanCmd extends AbstractCmd{

	public MakeCleanCmd() {
		super("/usr/bin/make");
	}
	
	@Override
	public void execute() throws IOException, InterruptedException {
		addArg("clean");
		
		newProcess();
	}

}
