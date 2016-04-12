package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.regex.PatternSyntaxException;

public class InvalidPropertyValueException extends RuntimeException {
    private final String key;
    private final String value;
    public InvalidPropertyValueException(String key, String value,String msg, PatternSyntaxException e) {
        super("Invalid Property value\n" + key + "=" + value +  "\n" + msg,e);
        this.key=key;
        this.value=value;
    }
    public String getPropertyKey() {
        return key;
    }
    
    public String getPropertyValue() {
        return value;
    }

}
