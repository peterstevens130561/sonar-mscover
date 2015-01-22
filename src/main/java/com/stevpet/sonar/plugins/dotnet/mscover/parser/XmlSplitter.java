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

public class XmlSplitter {
    
    XmlParserSubject xmlParserSubject ;
    
    public XmlSplitter(XmlParserSubject xmlParserSubject) {
        this.xmlParserSubject =xmlParserSubject;
    }
    
    public void setParser(XmlParserSubject xmlParserSubject) {
        this.xmlParserSubject =xmlParserSubject;   
    }
    
    Logger LOG = LoggerFactory.getLogger(XmlSplitter.class);
    

    public void splitIt(File file)  {
        BufferedReader rd = getBufferedReader(file);
        StopWatch sw = new StopWatch();
        sw.start();
        char[]  cbuf = new char[1024];
        StringBuilder sb = new StringBuilder((int) 10E6);
        while (readBuffer(rd, cbuf)!=-1) {
            String token = new String(cbuf);
            int index=token.indexOf("</Module>");
            if(index != -1) {
                int moduleEnd = index + 9;
                String toInclude = token.substring(0, moduleEnd);
                sb.append(toInclude);
                sb.append("</CoverageDSPriv>");
                xmlParserSubject.parseString(sb.toString());
                LOG.info("Found module");
                if(moduleEnd+1 < token.length()) {
                    sb=new StringBuilder((int)10E6);
                    sb.append("<CoverageDSPriv>");
                    String afterModuleEnd = token.substring(moduleEnd);
                    sb.append(afterModuleEnd);
                }
            } else {
                sb.append(token);
            }
        }
        if(sb.length() > 0) {
            xmlParserSubject.parseString(sb.toString());
            Log.info("done" + sb.length());
        }
        sw.stop();
        LOG.info("Reading took " + sw.getTime());
    }

    private int readBuffer(BufferedReader rd, char[] cbuf)  {
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
