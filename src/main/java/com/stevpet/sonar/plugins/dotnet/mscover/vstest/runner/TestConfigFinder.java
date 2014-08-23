package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

public interface TestConfigFinder {

    public abstract File getTestSettingsFileOrDie(String testSettings);

}