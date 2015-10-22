package com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ElementObserver {
    /**
     * path to match
     * @return
     */
    String path();
    
    enum Event {
        ENTRY,
        EXIT,
    }
    Event event();
}