package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.time.StopWatch;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

public abstract class XmlSplitterBaseProducer implements Runnable{

    protected File file;
    protected XmlSplitterQueue xmlSplitterQueue;
    private final static Logger LOG = LoggerFactory.getLogger(XmlSplitterProducer.class);
    private XmlModule xmlModule ;

    public XmlSplitterBaseProducer(File file, XmlModule xmlModule) {
        this.file=file;
        this.xmlModule=xmlModule;
    }

    @Override
    public void run() {
        BufferedReader rd = getBufferedReader(file);
        StopWatch sw = new StopWatch();
        sw.start();
        char[]  cbuf = new char[1024];
        long length=0;
        xmlModule.start();
        while (readBuffer(rd, cbuf)!=-1) {
            String token = new String(cbuf);
            int index=token.indexOf("</Module>");
            if(index != -1) {
                int moduleEnd = index + 9;
                String toInclude = token.substring(0, moduleEnd);
                length+=toInclude.length()+17;
                xmlModule.append(toInclude)
                    .append("</CoverageDSPriv>")
                    .queue();
                LOG.info("Found module");
                if(moduleEnd+1 < token.length()) {
                    xmlModule.start();
                    xmlModule.append("<CoverageDSPriv>");
                    String afterModuleEnd = token.substring(moduleEnd);
                    xmlModule.append(afterModuleEnd);
                }
            } else {
                length+=token.length();
                xmlModule.append(token);
            }
        }
        if(length > 0) {
            xmlModule.queue();
        }
        sw.stop();
        xmlSplitterQueue.waitTillDone();
        LOG.info("Reading took " + sw.getTime());
    }

    private int readBuffer(BufferedReader rd, char[] cbuf) {
        try {
            return rd.read(cbuf);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    }

    private BufferedReader getBufferedReader(File file) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new SonarException("File not found: " + file.getAbsolutePath(),e);
        }
        String charset="UTF-16";
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(inputStream,charset);
        } catch (UnsupportedEncodingException e) {
            throw new SonarException("Unsupported encoding " + charset,e);
        }
        BufferedReader rd = new BufferedReader(inputStreamReader);
        return rd;
    }

    
}