package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

public interface ResourceFilter {

    void setExclusions(String pattern);
    void setInclusions(String pattern);
    boolean isPassed(String longName);

}