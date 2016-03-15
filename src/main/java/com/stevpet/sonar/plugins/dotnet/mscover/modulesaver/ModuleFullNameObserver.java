package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;

abstract class ModuleFullNameObserver extends BaseParserObserver{

	abstract String getModuleName();

    /**
     * use to find out whether the SkippedDue attribute is set for this module
     * @return true if SkippedDue is present and is not empty
     */
    public boolean getSkipped() {
        // TODO Auto-generated method stub
        return false;
    }

}