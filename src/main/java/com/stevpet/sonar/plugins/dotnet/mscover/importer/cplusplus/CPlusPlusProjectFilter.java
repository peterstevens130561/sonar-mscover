package com.stevpet.sonar.plugins.dotnet.mscover.importer.cplusplus;

import java.io.File;
import java.io.FileFilter;

public class CPlusPlusProjectFilter implements FileFilter {

    public boolean accept(File file) {
        String path = file.getAbsolutePath();
        return path.endsWith(".vcxproj");
    }

}
