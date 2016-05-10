package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.InvalidTestResultException;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult.TestResult;

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
	                + getSonarStatus(detail.getTestResult()) + "\"");
	        testCaseDetails.append(" time=\""    + detail.getFormattedDuration() + "\"");
	        testCaseDetails.append(" name=\"");
	        testCaseDetails.append(detail.getTestName());
	        testCaseDetails.append("\">");
	        if (isFailed(detail)) {
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
	boolean isFailed(UnitTestMethodResult detail) {
	    return detail.getTestResult() == TestResult.Failed;
	}

	String getSonarStatus(TestResult outcome) {
	    String status ;
	    switch(outcome) {
	    case Passed : 
	        status="ok";
	        break;
	    case Failed : 
	        status="error" ;
	        break ;
	    case Ignored : 
	        status = "ignored" ;
	        break;
	     default:
	            throw new InvalidTestResultException(outcome);
	    }
	    return status;
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
