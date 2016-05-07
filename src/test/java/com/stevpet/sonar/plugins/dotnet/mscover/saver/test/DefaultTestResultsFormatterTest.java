package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;

public class DefaultTestResultsFormatterTest {
	private TestResultsFormatter testResultsFormatter = new DefaultTestResultsFormatter();
	private ClassUnitTestResult classUnitTestResult;
	
	@Before
	public void before() {
		classUnitTestResult = new ClassUnitTestResult(new File("bogus"));
	}
	
	@Test
	public void noResults_JustHeader() {
		String result=testResultsFormatter.formatClassUnitTestResults(classUnitTestResult);
		assertEquals("expect just the wrapper","<tests-details></tests-details>",result);
	}
	
	@Test
	public void passedtest_HeaderAndBody() {
		UnitTestMethodResult unitTest = new UnitTestMethodResult();
		unitTest.setTestName("TestMethod").setOutcome("Passed").setDuration(1000).setMessage("Message").setStackTrace("Stacktrace");
		classUnitTestResult.add(unitTest);
		String result=testResultsFormatter.formatClassUnitTestResults(classUnitTestResult);
		assertEquals("expect just the wrapper","<tests-details><testcase status=\"ok\" time=\"1.000\" name=\"TestMethod\"></testcase></tests-details>",result);
	}
	
	@Test
	public void failedtest_HeaderAndBody() {
		UnitTestMethodResult unitTest = new UnitTestMethodResult();
		unitTest.setTestName("TestMethod").setOutcome("Failed").setDuration(1000).setMessage("Message").setStackTrace("Stacktrace");
		classUnitTestResult.add(unitTest);
		String result=testResultsFormatter.formatClassUnitTestResults(classUnitTestResult);
		assertEquals("expect error message","<tests-details><testcase status=\"error\" time=\"1.000\" name=\"TestMethod\"><error message=\"Message\"><![CDATA[Stacktrace]]></error></testcase></tests-details>",result);
	}
}
