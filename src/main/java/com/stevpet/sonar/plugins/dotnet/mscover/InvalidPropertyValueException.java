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
    public InvalidPropertyValueException(String key, Integer value, String precondition) {
        super("Invalid Property value\n" + key + "=" + value +  "\n" + precondition);
        this.key=key;
        this.value=value.toString();
    }
    
    /**
     * Use to report a property that must be set, but is not
     * @param key
     * @param precondition of the property
     */
    public InvalidPropertyValueException(String key, String precondition) {
        super("Missing property\n" + key + "\n" + precondition);
        this.key=key;
        this.value=null;
    }
    public InvalidPropertyValueException(String key, String value, String precondition) {
        super("Invalid Property value\n" + key + "=" + value +  "\n" + precondition);
        this.key=key;
        this.value=value.toString();
    }
    public String getPropertyKey() {
        return key;
    }
    
    public String getPropertyValue() {
        return value;
    }

}
