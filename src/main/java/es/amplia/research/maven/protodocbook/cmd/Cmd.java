package es.amplia.research.maven.protodocbook.cmd;

import java.io.File;
import java.io.IOException;

public interface Cmd {

	void setConverterExec(File exec);	
	
	void setBaseDir(File dir);	
	
	void setInputDir(File dir);
	
	void setInputFile(File file);
	
	void setOutputDir(File dir);
	
	void setOutputFile(File file);
	
	public void execute() throws IOException, InterruptedException;
	
}
