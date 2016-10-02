
package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.UnitTestRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTestResultService;

public class DefaultUnitTestResultService implements UnitTestResultService {
    private final MethodRepository methodRepository;
    private final SourceFileRepository sourceFileRepository;
    private final UnitTestRepository unitTestRepository;

    public DefaultUnitTestResultService(MethodRepository methodRepository, SourceFileRepository sourceFileRepository, UnitTestRepository unitTestRepository) {
        this.methodRepository = methodRepository;
        this.sourceFileRepository = sourceFileRepository;
        this.unitTestRepository=unitTestRepository;
        
    }

    @Override
    public List<UnitTest> getUnitTestsFor( String filePath) {
        String fileId = sourceFileRepository.getId(filePath);
        MethodIds methods = methodRepository.getMethods(fileId);
        List<UnitTest> tests = unitTestRepository.getUnitTests(methods);
        return tests;
    }
}
