package com.stevpet.sonar.plugins.dotnet.utils;

import static org.mockito.Matchers.argThat;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

public class MsCoverTestUtils {
    public Pattern isPattern(String regex) {
        return argThat(new MsCoverTestUtils.IsPattern(regex));
    }
    
    public class IsPattern extends ArgumentMatcher<Pattern> {
        private String regex;
        public IsPattern(String regex) {
           this.regex=regex;
        }

        public boolean matches(Object value) {
            Pattern pattern=(Pattern)value;
            return regex.equals(pattern.toString());
        }
        @Override
        public void describeTo(Description description) {
            description.appendText(regex);
        }
    }
}
