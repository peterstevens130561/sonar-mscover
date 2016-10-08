package com.stevpet.sonar.plugins.dotnet.mscover.repositories;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

/**
 * Big bag of repositories
 * @author stevpet
 *
 */
public interface Repositories {
    MethodRepository getMethodRepository();
    SourceFileRepository getSourceFileRepository();
    UnitTestingResults getUnitTestingResults();
}
