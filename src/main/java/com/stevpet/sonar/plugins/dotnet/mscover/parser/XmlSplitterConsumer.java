package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.File;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSplitterConsumer implements Runnable{
    Logger LOG = LoggerFactory.getLogger(XmlSplitterConsumer.class);
    private XmlSplitterQueue queue ;
    private XmlParserSubject xmlParserSubject;
    private int instance;
    public XmlSplitterConsumer(XmlSplitterQueue queue,XmlParserSubject xmlParserSubject,int instance) {
        this.queue = queue;
        this.xmlParserSubject = xmlParserSubject;
        this.instance=instance;;
                
    }

    @Override
    public void run() {
        for(;;) {
            LOG.info("Parser {} rwaiting",instance);
            String fileName = queue.take();
            File file = new File(fileName);
            LOG.info("Parser {} running parsing segmennt in file",instance,file.getName());
            xmlParserSubject.parseFile(file);
        }
    }

}
