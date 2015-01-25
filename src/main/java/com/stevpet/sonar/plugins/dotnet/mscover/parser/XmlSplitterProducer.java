package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.File;

public class XmlSplitterProducer extends XmlSplitterBaseProducer {
    
    private StringBuilder sb ;
    private XmlParserSubject xmlParserSubject ;
    public XmlSplitterProducer(XmlSplitterQueue queue,File file) {
        super(file,new FileXmlModuleStrategy(queue));
        this.xmlSplitterQueue = queue;
        this.file = file;
    }

      
}
