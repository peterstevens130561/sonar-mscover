package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import static org.mockito.Mockito.when;
public class TestResultsParserMock extends GenericClassMock<TestResultsParser> {

	public TestResultsParserMock() {
		super(TestResultsParser.class);
	}

	public void givenTestResults(UnitTestRegistry testResults) {
		when(instance.getUnitTestRegistry()).thenReturn(testResults);
	}

}
