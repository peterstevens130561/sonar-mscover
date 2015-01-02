package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.ModuleFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public interface AbstractVsTestRunnerFactory {


    abstract VsTestRunner create();

    abstract VsTestRunner createBasicTestRunnner(MsCoverProperties propertiesHelper,
            ModuleFileSystem moduleFileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment);

}