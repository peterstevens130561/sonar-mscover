package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.IOException;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;

public interface Saver {

    /**
     * Save the registry into sonarQube
     * @throws IOException 
     */
    void save();
}