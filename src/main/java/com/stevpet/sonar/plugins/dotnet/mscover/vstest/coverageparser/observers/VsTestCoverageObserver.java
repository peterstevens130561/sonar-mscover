package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;

public abstract class VsTestCoverageObserver extends BaseParserObserver {

    public VsTestCoverageObserver() {
        super();
    }

    public abstract void setVsTestRegistry(VsTestRegistry vsTestRegistry);

}