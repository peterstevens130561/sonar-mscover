package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpecFlowTestResultsBuilder extends DefaultTestResultsBuilder {

    private final Logger LOG = LoggerFactory.getLogger(SpecFlowTestResultsBuilder.class);
    private SpecFlowScenarioMethodResolver specFlowScenarioMethodResolver;

    public SpecFlowTestResultsBuilder(FileNamesParser fileNamesParser, TestResultsParser testResultsParser,SpecFlowScenarioMethodResolver specFlowScenarioMethodResolver) {
        super(fileNamesParser, testResultsParser);
        this.specFlowScenarioMethodResolver = specFlowScenarioMethodResolver;
    }

    public static TestResultsBuilder create (
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment
			) {
		return new SpecFlowTestResultsBuilder (
				new OpenCoverFileNamesParser(), 
				new DefaultTestResultsParser(), 
				new SpecFlowScenarioMethodResolver(microsoftWindowsEnvironment)
				);
	}
   
    @Override
    protected String onNotFound(MethodId methodId) {
        String methodName=methodId.getMethodName();
        File  file=specFlowScenarioMethodResolver.getFile(methodName);
        if(file==null) {
            LOG.warn("Tried to resolve a potential specflow method, but failed {}",methodName);
        }
        return file==null?null:file.getAbsolutePath();
        
    }
}
