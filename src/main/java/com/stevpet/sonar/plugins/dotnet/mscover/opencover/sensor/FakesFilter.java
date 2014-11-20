package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.io.FilenameFilter;

public class FakesFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.toLowerCase().contains("fakes.");
    }
    
}