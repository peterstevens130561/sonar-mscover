package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class BaseParserObserver implements ParserObserver {

    private Pattern pattern;
    
    public final boolean isMatch(String path) {
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();      
    }

    /**
     * Pattern to use when matching
     * @param regex to use. observeElement and observAttribute will be invoked for paths matching the regex
     */
    protected void setPattern(String regex) {
        pattern = Pattern.compile(regex);
    }
    

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver#observeElement(java.lang.String, java.lang.String)
     */
    public void observeElement(String name, String text) {

    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver#observeAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void observeAttribute(String elementName, String path,
            String attributeValue, String attributeName) {
        // to be overridden when needed
    }

}
