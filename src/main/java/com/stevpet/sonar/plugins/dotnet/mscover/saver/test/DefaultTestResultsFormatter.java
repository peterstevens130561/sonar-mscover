package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;

public class DefaultTestResultsFormatter implements TestResultsFormatter {

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter#formatClassUnitTestResults(com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult)
	 */
	@Override
	public String formatClassUnitTestResults(ClassUnitTestResult fileResults) {
		StringBuilder testCaseDetails = new StringBuilder(256);
	    testCaseDetails.append("<tests-details>");
	    List<UnitTestMethodResult> details = fileResults.getUnitTests();
	    for (UnitTestMethodResult detail : details) {
	        testCaseDetails.append("<testcase status=\""
	                + getSonarStatus(detail.getOutcome()) + "\"");
	        testCaseDetails.append(" time=\"0\"");
	        testCaseDetails.append(" name=\"");
	        testCaseDetails.append(detail.getTestName());
	        testCaseDetails.append("\">");
	        if (isNotPassed(detail)) {
	            testCaseDetails.append("<error ");
	            testCaseDetails.append(getMessageAttribute(detail));
	            testCaseDetails.append(">");
	            testCaseDetails.append("<![CDATA[");
	            testCaseDetails.append(
	                    StringEscapeUtils.escapeXml(detail.getStackTrace()))
	                    .append("]]>");
	            testCaseDetails.append("</error>");
	        }
	        testCaseDetails.append("</testcase>");
	    }
	    testCaseDetails.append("</tests-details>");
	    String data = testCaseDetails.toString();
		return data;
	}
	boolean isNotPassed(UnitTestMethodResult detail) {
	    return !"Passed".equals(detail.getOutcome());
	}

	String getSonarStatus(String outcome) {
	    if ("Passed".equals(outcome)) {
	        return "ok";
	    } else {
	        return "error";
	    }
	}

	String getMessageAttribute(UnitTestMethodResult result) {
	    String errorMessage = result.getMessage();
	    String xmlErrorMessage = StringEscapeUtils.escapeXml(errorMessage);
	    StringBuilder sb = new StringBuilder();
	    sb.append("message=\"");
	    sb.append(xmlErrorMessage);
	    sb.append("\"");
	    return sb.toString();
	}



}
