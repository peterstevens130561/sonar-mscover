package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;


public class AlwaysPassThroughResourceFilter implements ResourceFilter {

    public void setExclusions(String pattern) { 
        //Intentionally left blank
    }

    public boolean isPassed(String longName) {

        return true ;
    }

    public void setInclusions(String pattern) {      
        //Intentionally left blank
    }

    public boolean isIncluded(String longName) {
        return true;
    }

}
