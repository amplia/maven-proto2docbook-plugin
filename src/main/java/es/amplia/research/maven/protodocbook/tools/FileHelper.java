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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class FileHelper {

    private static Log log = new SystemStreamLog();
    
    public static String readFile(File f) {
        StringBuilder contents = new StringBuilder();
        try {
            BufferedReader input = new BufferedReader(new FileReader(f));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }
    
    public static String getFileExtension(File _file){
        String name = _file.getName();
        int k = name.lastIndexOf(".");
        String ext = "";
        if(k != -1)
            ext = name.substring(k + 1, name.length());
        return ext;
    }
    
    
    public static boolean copy(File _in, File _out){
        try {
            FileUtils.copyFile(_in, _out);
            return true;
        } catch (IOException e) {
            log.error("File copy error",e);
            return false;
        }      
    }
    
    public static boolean copyDir(File _in, File _out){
        try {
            log.info("Copying "+ _in.getName() + " to " + _out.getAbsolutePath());
            FileUtils.copyDirectory(_in, _out);
            return true;
        } catch (IOException e) {
            log.error("File copy error",e);
            return false;
        }      
    }
    
}

