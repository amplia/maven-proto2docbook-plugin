package es.amplia.research.maven.protodocbook;

import java.io.File;
import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import es.amplia.research.maven.protodocbook.cmd.Cmd;
import es.amplia.research.maven.protodocbook.cmd.Factory;

/**
 * @Mojo (name = "proto2docbook", defaultPhase =LifecyclePhase.COMPILE,threadSafe=true)
 * @author cbadenes
 * @Execute( goal = "proto2docbook", phase = LifecyclePhase.COMPILE, lifecycle="<lifecycle-id>" )
 * 
 */
@Mojo(name = "proto2docbook", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true)
public class PluginMojo extends AbstractMojo {

	@Getter
	@Setter
	@Parameter(property = "basedir", defaultValue = "${project.directory}", required = false)
	private File basedir;
	
	@Getter
	@Setter
	@Parameter(property = "outdir", defaultValue = "${project.build.directory}/protoc", required = false)
	private File outdir;

	@Getter
	@Setter
	@Parameter(property = "indir", defaultValue = "src/main/protobuf", required = false)
	private File indir;

	@Getter
	@Setter
	@Parameter(property = "protoc", defaultValue = "/usr/local/bin/protoc", required = false)
	private String protoc;
	
	@Getter
	@Setter
	@Parameter(property = "hierarchical", defaultValue = "true", required = false)
	private Boolean hierarchical;

	private PluginConverter converter;

	public PluginMojo() {
		this.converter = new PluginConverter();
	}

	private static final String temporal_file = "docbook_out.xml";
	
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			if (!converter.isCompiled()) {
				Cmd makeCmd = Factory.newCommand("make");
				makeCmd.setBaseDir(new File(converter.getHomeDir(),"linux"));
				makeCmd.execute();				
			}
			
			
			Iterator<File> files = FileUtils.iterateFiles(indir, new String[] {"proto"}, true);
					
			while(files.hasNext()) {
				File file = files.next();
				
				Cmd protocCmd = Factory.newCommand("protoc");
				protocCmd.setConverterExec(this.converter.getExec());
				protocCmd.setBaseDir(basedir);
				protocCmd.setInputDir(indir);
				protocCmd.setInputFile(file);
				protocCmd.setOutputDir(outdir);
				protocCmd.execute();
			
				String fileName = hierarchical? 
						StringUtils.replace(StringUtils.substringAfter(file.getAbsolutePath(), indir.getAbsolutePath()),".proto",".xml"):
						StringUtils.replace(file.getName(), ".proto", ".xml") ;
				FileUtils.copyFile(new File(outdir,temporal_file), new File(outdir,fileName));
				
			}			
		
			File tempFile = new File(outdir,temporal_file);
			if (tempFile.exists()) FileUtils.forceDelete(tempFile);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException(e.getMessage());
		}

	}
	

}
