package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

import org.sonar.api.resources.Resource;

public class FileResourceFilter  {
    private String includedPaths;
    private String excludedPaths;

    /**
     * neither included, nor excluded: true
     * not included, excluded set, if it matches excluded then false, otherwise true
     * included set, excloded not set, if it matches included then true, otherwise false
     * both set: if it matches excluded then false. if it matches included, then true, otherwise false
     * 
     * @param resource
     * @return
     */
    public boolean isIncludedFileResource(Resource<?> resource) {
        if(includedPaths ==null && excludedPaths==null) {
            return true;
        }
        return true ;
    }
    
    /**
     * 
     * @param includedPaths
     */
    public void setIncluded(String includedPaths) {
        this.includedPaths = includedPaths;
    }
    
    public void setExcluded(String excludedPaths) {
        this.excludedPaths = excludedPaths;
    }
}
