package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;

public class DefaultTestResultsFormatterTest {
	private TestResultsFormatter testResultsFormatter = new DefaultTestResultsFormatter();
	
	@Test
	public void noResults_JustHeader() {
		ClassUnitTestResult classUnitTestResult = new ClassUnitTestResult();
		String result=testResultsFormatter.formatClassUnitTestResults(classUnitTestResult);
		assertEquals("expect just the wrapper","<tests-details></tests-details>",result);
	}
	
	@Test
	public void passedtest_HeaderAndBody() {
		ClassUnitTestResult classUnitTestResult = new ClassUnitTestResult();
		UnitTestMethodResult unitTest = new UnitTestMethodResult();
		unitTest.setTestName("TestMethod").setOutcome("Passed").setDuration("1ms").setMessage("Message").setStackTrace("Stacktrace");
		classUnitTestResult.add(unitTest);
		String result=testResultsFormatter.formatClassUnitTestResults(classUnitTestResult);
		assertEquals("expect just the wrapper","<tests-details><testcase status=\"ok\" time=\"0\" name=\"TestMethod\"></testcase></tests-details>",result);
	}
	
	@Test
	public void failedtest_HeaderAndBody() {
		ClassUnitTestResult classUnitTestResult = new ClassUnitTestResult();
		UnitTestMethodResult unitTest = new UnitTestMethodResult();
		unitTest.setTestName("TestMethod").setOutcome("Failed").setDuration("1ms").setMessage("Message").setStackTrace("Stacktrace");
		classUnitTestResult.add(unitTest);
		String result=testResultsFormatter.formatClassUnitTestResults(classUnitTestResult);
		assertEquals("expect error message","<tests-details><testcase status=\"error\" time=\"0\" name=\"TestMethod\"><error message=\"Message\"><![CDATA[Stacktrace]]></error></testcase></tests-details>",result);
	}
}
