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
     * invoke at construction to set the pattern to match
     * @param regex
     */
    protected void setPattern(String regex) {
        pattern = Pattern.compile(regex);
    }
    
    /**
     * Override for own implementation
     */
    public void observeElement(String name, String text) {

    }


    public void observeAttribute(String elementName, String path,
            String attributeValue, String attributeName) {

    }

}
