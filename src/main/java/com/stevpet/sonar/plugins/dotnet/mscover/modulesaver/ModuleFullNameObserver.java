package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;

public abstract class ModuleFullNameObserver extends BaseParserObserver{

	abstract String getModuleName();

}