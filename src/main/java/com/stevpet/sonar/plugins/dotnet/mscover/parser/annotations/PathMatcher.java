package com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
/**
 * use to match a literal path, i.e. when there are multiple elements with the same name
 *
 */
public @interface PathMatcher {
    String path();
}
