package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

public interface TestConfigFinder {

    public abstract File getTestSettingsFileOrDie(String testSettings);

}