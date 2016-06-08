package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;

public abstract class ModuleSummaryObserver extends BaseParserObserver {

    /**
     * 
     * @return true if nothing is covered
     */
    abstract boolean isNotCovered();

}