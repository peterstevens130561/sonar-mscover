package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;

public abstract class OpenCoverObserver extends BaseParserObserver {

    private SonarCoverage registry;

    public OpenCoverObserver() {
        super();
    }

    public void setRegistry(SonarCoverage registry) {
        this.registry = registry;
    }

    /**
     * @return the registry
     */
    protected SonarCoverage getRegistry() {
        return registry;
    }

}