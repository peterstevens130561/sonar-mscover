package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class SpecFlowTestResultsBuilder extends DefaultTestResultsBuilder {

    private final Logger LOG = LoggerFactory.getLogger(SpecFlowTestResultsBuilder.class);
    private SpecFlowScenarioMethodResolver specFlowScenarioMethodResolver;

    public SpecFlowTestResultsBuilder(FileNamesParser fileNamesParser, TestResultsParser testResultsParser,SpecFlowScenarioMethodResolver specFlowScenarioMethodResolver) {
        super(fileNamesParser, testResultsParser);
        this.specFlowScenarioMethodResolver = specFlowScenarioMethodResolver;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String onNotFound(MethodId methodId) {
        String methodName=methodId.getMethodName();
        File  file=specFlowScenarioMethodResolver.getFile(methodName);
        if(file==null) {
            LOG.warn("Could not find file for {}",methodName);
        }
        return file==null?null:file.getAbsolutePath();
        
    }
}
