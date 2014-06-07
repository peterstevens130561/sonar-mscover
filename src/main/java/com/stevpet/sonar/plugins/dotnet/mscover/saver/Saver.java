package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.IOException;

public interface Saver {

    /**
     * Save the registry into sonarQube
     * @throws IOException 
     */
    void save();
}