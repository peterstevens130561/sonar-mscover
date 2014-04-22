package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.sonar.api.utils.SonarException;

import com.google.common.io.Files;

public class VSTestResults {

    private static String RESULTS_PATTERN = "\nResults File: ";
    private static String ATTACHMENTS_PATTERN = "\nAttachments:\r\n  ";
    private File resultsFile;

    public void setFile(File resultsFile) {
       this.resultsFile=resultsFile;
        
    }


    public String getResultsPath() throws IOException {
        return  getFile(RESULTS_PATTERN);

    }

    public String getCoveragePath() throws IOException {
        return getFile(ATTACHMENTS_PATTERN);
    }
    private String getFile(String begin) throws IOException {
        String results=Files.toString(resultsFile, Charset.defaultCharset());
        String msg = "invalid results file '" + resultsFile.getCanonicalPath() + "', can 't find ";
        int resultsPos=results.lastIndexOf(begin);
        if(resultsPos==-1) {
            throw new SonarException(msg + begin);
        }
        int captureStart=resultsPos+begin.length();
        int newLinePos=results.indexOf("\r\n", captureStart);
        if(newLinePos == -1) {
            throw new SonarException(msg + " end of line");
        }
        String result=results.substring(resultsPos + begin.length(),newLinePos);
        return result;
    }


}
