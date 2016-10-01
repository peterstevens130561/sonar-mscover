
package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTestRepository;
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
