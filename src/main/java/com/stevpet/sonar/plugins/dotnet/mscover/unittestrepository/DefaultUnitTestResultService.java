package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.UnitTestRepository;

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
        List<MethodId> methods = methodRepository.getMethods(fileId);
        List<UnitTest> tests = unitTestRepository.getUnitTests(methods);
        return tests;
    }
}
