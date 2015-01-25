package com.stevpet.sonar.plugins.dotnet.mscover.parser;

public class StringXmlModuleStrategy implements XmlModule {

    XmlSplitterQueue queue;
    StringBuilder sb ;
    
    public StringXmlModuleStrategy(XmlSplitterQueue queue) {
        this.queue = queue;
    }
    
    @Override
    public XmlModule start() {
        sb = new StringBuilder((int)10E6);
        return this;
    }

    @Override
    public XmlModule append(String string) {
        sb.append(string);
        return this;
    }

    @Override
    public XmlModule queue() {
        queue.put(sb.toString());
        return this;
    }

}
