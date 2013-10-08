package es.amplia.research.maven.protodocbook.cmd;

import java.io.IOException;

public class ProtocCmd extends AbstractCmd{

	public ProtocCmd() {
		super("/usr/local/bin/protoc");
	}
	
	@Override
	/**
	 * execute(new File("."),
    			protocCmd,
				"-I/usr/include",
				"--proto_path="+baseDir.getAbsolutePath(),
				proto.getAbsolutePath(),
				"--plugin="+this.homeDir.getAbsolutePath()+"/linux/protoc-gen-docbook",
				"--docbook_out="+outDir.getAbsolutePath());    
	 */
	public void execute() throws IOException, InterruptedException {
		addArg("-I/usr/include");
		addArg("--proto_path="+ inputDir.getAbsolutePath());
		addArg( inputFile.getAbsolutePath());
		addArg("--plugin="+converterExec.getAbsolutePath());
		addArg("--docbook_out="+outputDir.getAbsolutePath());
		
		newProcess();
		
		
	}

}
