package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SonarCoverage {
    Map<String,SonarFileCoverage> map = new HashMap<String,SonarFileCoverage>();

    /**
     * 
     * @param id of file
     * @return either a new FileCoverage, or the one found
     */
    public SonarFileCoverage getCoveredFile(String id) {
        if( !map.containsKey(id)) {
            map.put(id, new SonarFileCoverage()) ;
        }
        return map.get(id);
    }

    public Collection<SonarFileCoverage> getValues() {
        return map.values();
    }

    /**
     * 
     * @return number of files listed in the coverage report
     */
    public int size() {
        return map.size();
    }

}
