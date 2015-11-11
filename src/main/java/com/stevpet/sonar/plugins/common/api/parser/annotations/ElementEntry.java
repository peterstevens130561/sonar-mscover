package com.stevpet.sonar.plugins.common.api.parser.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ElementEntry {
    /**
     * path to match
     * @return
     */
    String path();
}