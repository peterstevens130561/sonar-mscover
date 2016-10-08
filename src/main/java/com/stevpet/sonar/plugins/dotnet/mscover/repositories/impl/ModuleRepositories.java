package com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.Repositories;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;

public class ModuleRepositories implements Repositories, BatchExtension {

    private final MethodRepository methodRepository;
    private final SourceFileRepository sourceFileRepository;
    private final UnitTestingResults unitTestingResults;

    public ModuleRepositories() {
        this(new DefaultMethodRepository(), new DefaultSourceFileRepository(), new UnitTestingResults());
    }

    ModuleRepositories(MethodRepository methodRepository, SourceFileRepository sourceFileRepository,
            UnitTestingResults unitTestRegistry) {
        this.methodRepository = methodRepository;
        this.sourceFileRepository = sourceFileRepository;
        this.unitTestingResults= unitTestRegistry;
    }

    @Override
    public MethodRepository getMethodRepository() {
        return methodRepository;
    }

    @Override
    public SourceFileRepository getSourceFileRepository() {
        return sourceFileRepository;
    }


    @Override
    public UnitTestingResults getUnitTestingResults() {
        return unitTestingResults;
    }

}
