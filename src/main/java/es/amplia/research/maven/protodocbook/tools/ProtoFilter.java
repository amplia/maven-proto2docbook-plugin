package es.amplia.research.maven.protodocbook.tools;

import java.io.File;
import java.io.FilenameFilter;

public class ProtoFilter implements FilenameFilter{

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(".proto");
	}

}
