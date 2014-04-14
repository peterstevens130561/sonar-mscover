package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface AttributeMatcher {

    String attributeName();
    String elementName();

}
