package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.UnitTestRepository;

public class UnitTestResultService {

    public ProjectUnitTestResults getUnitTestsFor(MethodRepository methodRepository, SourceFileRepository sourceFileRepository, UnitTestRepository unitTestRepository, String filePath) {
        ProjectUnitTestResults results= new ProjectUnitTestResults();
        String fileId = sourceFileRepository.getId(filePath);
        List<MethodId> methods = methodRepository.getMethods(fileId);
        List<UnitTest> tests = unitTestRepository.getUnitTests(methods);
        return results;
    }
}
