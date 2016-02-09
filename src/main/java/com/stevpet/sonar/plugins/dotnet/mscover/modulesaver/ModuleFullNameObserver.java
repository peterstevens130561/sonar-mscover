package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;

abstract class ModuleFullNameObserver extends BaseParserObserver{

	abstract String getModuleName();

}