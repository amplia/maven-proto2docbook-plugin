/*******************************************************************************
 * Copyright (c) 2013 Carlos Badenes Olmedo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributor(s):
 *     cbadenes
 ******************************************************************************/
package es.amplia.research.maven.protodocbook.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.net.URISyntaxException;
import java.util.Map;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import es.amplia.research.maven.protodocbook.tools.ZipHelper;

public class Factory {

    Log log = new SystemStreamLog();

    private File homeDir;

	private Runtime rt;

	private String[] env;

	@Setter
	private String protocCmd;
	
	@Setter
	private String fopCmd;
	
	public Factory() {
        this.rt = Runtime.getRuntime();
        this.env = new String[] {};
        File target = new File("target");
    	target.mkdir();
    }

    
    
    @SuppressWarnings("unchecked")
	public static Cmd newCommand(String cmd) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    	
    	String packageName 	= Factory.class.getPackage().getName();
    	
    	String className	= new StringBuilder().append(packageName).append(".").append(StringUtils.capitalize(cmd)).append("Cmd").toString(); 
    	
    	Class<Cmd> cmdClass 	= ((Class<Cmd>) Class.forName(className));
    	
    	return cmdClass.newInstance();
    }

    public File getHomeDir() {
    	return homeDir;
    }
    
    public void makeClean() throws Exception {
    	execute(new File(this.homeDir, "linux"),"/usr/bin/make","clean");
    }
    
    public void make() throws Exception  {
    	execute(new File(this.homeDir, "linux"),"/usr/bin/make");
    }
    
    public void gen(File baseDir, File proto, File outDir) throws Exception  {
    	execute(new File("."),
    			protocCmd,
				"-I/usr/include",
				"--proto_path="+baseDir.getAbsolutePath(),
				proto.getAbsolutePath(),
				"--plugin="+this.homeDir.getAbsolutePath()+"/linux/protoc-gen-docbook",
				"--docbook_out="+outDir.getAbsolutePath());    	
    }
    
    public void convert(File baseDir, File proto, File outDir) throws Exception  {
    	execute(new File("."),
    			fopCmd,
				"-xml",
				outDir.getAbsolutePath()+"/docbook_out.xml",
				"-xsl",
				"/usr/share/xml/docbook/stylesheet/docbook-xsl/fo/docbook.xsl",
				"-pdf",
				outDir.getAbsolutePath()+"/docbook_out.pdf",
				"-param",
				"page.orientation",
				"landscape",
				"-param",
				"paper.type",
				"USletter");    	
    }
    
    public void executeAll() throws IOException, InterruptedException {
    	
    	
    	File target = new File("target");
    	target.mkdir();
    	
    	ProcessBuilder pb = new ProcessBuilder("/usr/bin/make", 
    											"clean");
		Map<String, String> env = pb.environment();
		pb.directory(new File(homeDir,"linux"));
		File logFile = new File("log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(logFile));
		Process p = pb.start();
		p.waitFor();
		
		pb = new ProcessBuilder("/usr/bin/make");
		pb.directory(new File(homeDir,"linux"));
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(logFile));
		p = pb.start();
		p.waitFor();
		
		pb = new ProcessBuilder("/usr/local/bin/protoc",
								"-I/usr/include",
								"--proto_path=src/main/protobuf",
								"src/main/protobuf/sample.proto",
								"--plugin="+this.homeDir.getAbsolutePath()+"/linux/protoc-gen-docbook",
								"--docbook_out=target");
		pb.directory(new File("."));
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(logFile));
		p = pb.start();
		p.waitFor();
		
		pb = new ProcessBuilder("/usr/bin/fop",
				"-xml",
				"target/docbook_out.xml",
				"-xsl",
				"/usr/share/xml/docbook/stylesheet/docbook-xsl/fo/docbook.xsl",
				"-pdf",
				"target/docbook_out.pdf",
				"-param",
				"page.orientation",
				"landscape",
				"-param",
				"paper.type",
				"USletter");
		pb.directory(new File("."));
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(logFile));
		p = pb.start();
		p.waitFor();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ( (line = br.readLine()) != null) {
		   if (this.log.isInfoEnabled()) this.log.info(line); 	 
		}		
    }
    
    private void execute(File directory,String...cmd) throws Exception {
    	ProcessBuilder pb = new ProcessBuilder(cmd);
		Map<String, String> env = pb.environment();
		pb.directory(directory);
		pb.redirectErrorStream(true);		
		Process p = pb.start();
		p.waitFor();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ( (line = br.readLine()) != null) {
		   if (this.log.isInfoEnabled()) this.log.info(line); 	 
		}	
    }
    
}
