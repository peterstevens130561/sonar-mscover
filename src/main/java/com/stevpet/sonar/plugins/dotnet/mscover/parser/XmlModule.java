package com.stevpet.sonar.plugins.dotnet.mscover.parser;

public interface XmlModule {
    /*
     * prepare to start collecting data
     */
    public XmlModule start();
    /*
     * add the string
     */
    public XmlModule append(String string);
    /*
     * queue the current string for processing
     */
    public XmlModule queue();
}
