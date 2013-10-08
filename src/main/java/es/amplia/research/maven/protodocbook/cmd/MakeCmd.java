package es.amplia.research.maven.protodocbook.cmd;

import java.io.IOException;

public class MakeCmd extends AbstractCmd{

	public MakeCmd() {
		super("/usr/bin/make");
	}
	
	@Override
	public void execute() throws IOException, InterruptedException {
		newProcess();
	}

}
