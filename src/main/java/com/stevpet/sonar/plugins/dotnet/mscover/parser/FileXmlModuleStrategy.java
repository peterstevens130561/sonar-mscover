package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.sonar.api.utils.SonarException;

public class FileXmlModuleStrategy implements XmlModule {
    XmlSplitterQueue queue;
    BufferedWriter writer;
    File tempFile;
    public FileXmlModuleStrategy(XmlSplitterQueue queue) {
        this.queue = queue;
    }
    
    @Override
    public XmlModule start() {
        writer=getWriter();
        return this;
    }

    @Override
    public XmlModule append(String string) {
        try {
            writer.append(string);
        } catch (IOException e) {
            throw new SonarException("IOException while writing reason:" + e.getMessage());
        }
        return this;
    }

    @Override
    public XmlModule queue() {
        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        queue.put(tempFile.getAbsolutePath());
        return this;
    }
    
    public BufferedWriter getWriter() {
        String tmpDir=System.getenv("TMP");
        tempFile=new File(tmpDir,System.currentTimeMillis() + ".xml");
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        FileWriter fw=null;
        try {
            fw = new FileWriter(tempFile.getAbsoluteFile());
        } catch (IOException e) {
            throw new SonarException("Could not create FileWriter",e);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        return bw;
    }

}
