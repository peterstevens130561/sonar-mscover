package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;

public interface FakesRemover {

    void removeFakes(File myDir);

}