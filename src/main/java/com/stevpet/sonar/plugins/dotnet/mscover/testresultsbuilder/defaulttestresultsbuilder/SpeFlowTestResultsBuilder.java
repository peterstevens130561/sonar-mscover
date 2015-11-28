package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpeFlowTestResultsBuilder extends SpecFlowTestResultsBuilderBase
		implements BatchExtension {

	public SpeFlowTestResultsBuilder(
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment
			) {
		super(
				new OpenCoverFileNamesParser(), 
				new DefaultTestResultsParser(), 
				new SpecFlowScenarioMethodResolver(microsoftWindowsEnvironment)
				);
	}

}
