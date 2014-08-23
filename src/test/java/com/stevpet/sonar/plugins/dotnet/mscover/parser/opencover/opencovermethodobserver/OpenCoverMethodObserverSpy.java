package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMethodObserver;

public class OpenCoverMethodObserverSpy extends OpenCoverMethodObserver {

    public String getNameSpaceName() {
        return super.nameSpaceName;
    }

    public String getClassName() {
        return super.className;
    }

    public String getMethodName() {
        return super.methodName;
    }

    public String getModuleName() {
        return super.moduleName;
    }

    public boolean isSkipping() {
        return scanMode == ScanMode.SKIP;
    }

}
