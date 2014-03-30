package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

import org.sonar.api.utils.WildcardPattern;

public class AntPatternResourceFilter implements ResourceFilter {
    private WildcardPattern[] exclusionMatchers;
    private WildcardPattern[] inclusionMatchers;
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.FileFilter#setExclusions(java.lang.String)
     */
    public void setExclusions(String pattern) {
        exclusionMatchers=createMatchers(pattern);   
    }
    
    public void setInclusions(String pattern) {
        inclusionMatchers=createMatchers(pattern);      
    }

    private WildcardPattern[] createMatchers(String pattern) {
        if(pattern==null) {
            return null;
        }
        String[] patterns = pattern.split(",");
        return WildcardPattern.create(patterns);         
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.FileFilter#isPassed(java.lang.String)
     */
    public boolean isPassed(String longName) {
        if(exclusionMatchers==null) {
            return true;
        }
        boolean isExcluded=WildcardPattern.match(exclusionMatchers,longName);
        if(isExcluded) {
            return false ;
        }

        if(inclusionMatchers == null) {
            return true;
        }
        return WildcardPattern.match(inclusionMatchers,longName);

    }

}
