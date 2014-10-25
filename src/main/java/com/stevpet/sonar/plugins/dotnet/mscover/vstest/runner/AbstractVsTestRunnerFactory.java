package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public interface AbstractVsTestRunnerFactory {


    abstract VsTestRunner create();

    abstract VsTestRunner createBasicTestRunnner(MsCoverProperties propertiesHelper,
            ModuleFileSystem moduleFileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment);

}