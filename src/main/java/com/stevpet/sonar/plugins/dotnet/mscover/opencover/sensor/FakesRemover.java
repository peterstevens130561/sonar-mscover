package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;

public interface FakesRemover {

    int removeFakes(File myDir);

}