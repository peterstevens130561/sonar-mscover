package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Property {

    Pattern pattern = Pattern.compile("^([^=]+)=(.*)");
    String key;
    String value;

    public Property(String line) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a valid property syntax:" + line);
        }
        key = matcher.group(1);
        value = matcher.group(2);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
