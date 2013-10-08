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
package es.amplia.research.maven.protodocbook.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class ZipHelper {

    Log log;
    
    public ZipHelper() {
        this.log = new SystemStreamLog();
    }
   
    public File unzipEntry(File _zipFile, String _name ) {
        File directory = null;
        try {
            if (this.log.isDebugEnabled())
                this.log.debug("zip file: " + _zipFile.getAbsolutePath());
            ZipEntry zipEntry = null;
            String zipEntryName = null;
            ZipFile jarZipFile = new ZipFile(_zipFile);
            Enumeration<? extends ZipEntry> e = jarZipFile.entries();
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                zipEntryName = zipEntry.getName();
                if (zipEntryName.startsWith(_name) && zipEntryName.endsWith(".zip")) {
                    if (this.log.isInfoEnabled())
                        this.log.info("Found in " + zipEntryName);
                    directory = new File(_zipFile.getParent(), FilenameUtils.removeExtension(zipEntryName));
                    break;
                }
            }
            if (directory != null && !directory.exists()) {
                unzipEntry(jarZipFile, zipEntry, _zipFile.getParentFile());
                File asciiDocArchive = new File(_zipFile.getParent(), zipEntryName);
                unzipArchive(asciiDocArchive, _zipFile.getParentFile());
                asciiDocArchive.deleteOnExit();
            }

        } catch (ZipException ze) {
            this.log.error(ze.getMessage(), ze);
        } catch (IOException ioe) {
            this.log.error(ioe.getMessage(), ioe);
        }
        return directory;
    }

    public void unzipArchive(File archive, File outputDir) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, outputDir);
            }
        } catch (Exception e) {
            this.log.error("Error while extracting file " + archive, e);
        }
    }

    public void unzipEntry(ZipFile zipfile, ZipEntry zipEntry, File outputDir) throws IOException {
        if (zipEntry.isDirectory()) {
            createDir(new File(outputDir, zipEntry.getName()));
            return;
        }
        File outputFile = new File(outputDir, zipEntry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }
        
        if (this.log.isDebugEnabled())
            this.log.debug("Extracting " + zipEntry);
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(zipEntry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            outputStream.close();
            inputStream.close();
        }
        
        if ((outputFile != null) && (!outputFile.isDirectory() && FileHelper.getFileExtension(outputFile).equalsIgnoreCase("py"))){
            outputFile.setExecutable(true);
        }
    }   
    

    private void createDir(File dir) {
        if (this.log.isDebugEnabled())
            this.log.debug("Creating dir " + dir.getName());
        if (!dir.mkdirs())
            throw new RuntimeException("Can not create dir " + dir);
    }
    
}

