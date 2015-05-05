package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class NullTestResultsSaver implements TestResultsSaver {
    private final static Logger LOG = LoggerFactory
            .getLogger(NullTestResultsSaver.class);

    @Override
    public void save(ProjectUnitTestResults projetUnitTestResults) {
        LOG.info("Invoked");
    }

}
