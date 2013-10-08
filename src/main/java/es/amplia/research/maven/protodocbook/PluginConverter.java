package es.amplia.research.maven.protodocbook;

import java.io.File;
import java.net.URISyntaxException;

import lombok.Getter;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import es.amplia.research.maven.protodocbook.cmd.Factory;
import es.amplia.research.maven.protodocbook.tools.ZipHelper;

/**
 * 
 * @author cbadenes
 *
 */
public class PluginConverter {

	Log log = new SystemStreamLog();
	
	@Getter
	private File homeDir;
	
	public PluginConverter() {
		this.homeDir = findAsciidocHome();
	}
	
	
	private File findAsciidocHome() {
        try {
            ZipHelper helper = new ZipHelper();
            File jarFile = new File(Factory.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File home = helper.unzipEntry(jarFile, "protoc-gen-docbook");
            if (this.log.isInfoEnabled())
                this.log.info("ProtocGenDocbookDocHome: " + home);
            return home;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error getting jar file path",e);
        }
    }
	
	public File getExec() {
		return new File(homeDir,"linux/protoc-gen-docbook");
	}
	
	public boolean isCompiled() {
		return getExec().exists();
	}
}
