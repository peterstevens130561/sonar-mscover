package com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserData;


public abstract class BaseParserObserver implements ParserObserver {

    private Pattern pattern;
    private boolean hasError =false;
    private ParserData parserData;
    protected void setError() {
        hasError=true;
    }
    
    public void injectParserData(ParserData parserData) {
        this.parserData = parserData;
    }
    
    public void setSkipTillNextElement(String elementName) {
        parserData.skipTillNextElement(elementName);
    }
    public boolean hasError() {
        return hasError;
    }
    public final boolean isMatch(String path) {
        if(pattern==null) {
            throw new IllegalArgumentException("No patterns defined (did you use setPattern in the creation of your observer?) ");
        }
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
