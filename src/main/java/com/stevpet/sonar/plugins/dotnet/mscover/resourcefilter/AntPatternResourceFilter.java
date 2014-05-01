package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

import org.apache.commons.lang.StringUtils;
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
        if(StringUtils.isEmpty(longName)) {
            return false;
        }
        String antName = longName.replaceAll("\\\\", "/");
        if(exclusionMatchers==null) {
            return true;
        }
        boolean isExcluded=WildcardPattern.match(exclusionMatchers,antName);
        if(isExcluded) {
            return false ;
        }

        if(inclusionMatchers == null) {
            return true;
        }
        return WildcardPattern.match(inclusionMatchers,antName);
    }
    
    public boolean isIncluded(String longName) {
        String antName = longName.replaceAll("\\\\", "/");
        if(inclusionMatchers == null) {
            return false;
        }
        return WildcardPattern.match(inclusionMatchers,antName);
    }
}
