package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.sonar.api.batch.fs.FileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public interface AbstractVsTestRunnerFactory {


    VsTestRunner create();

    VsTestRunner createBasicTestRunnner(MsCoverProperties propertiesHelper,
            FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment);

}