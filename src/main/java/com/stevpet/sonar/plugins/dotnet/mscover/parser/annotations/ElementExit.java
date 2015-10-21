package com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ElementExit {
    /**
     * path to match
     * @return
     */

    String path();
}