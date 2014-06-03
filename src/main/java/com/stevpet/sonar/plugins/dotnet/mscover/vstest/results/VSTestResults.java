package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.sonar.api.utils.SonarException;

import com.google.common.io.Files;

public class VSTestResults {

    private String results;
    private static String RESULTS_PATTERN = "\nResults File: ";
    private static String ATTACHMENTS_PATTERN = "\nAttachments:";

    public void setFile(File resultsFile) {
       try {
        results=Files.toString(resultsFile, Charset.defaultCharset());
    } catch (IOException e) {
        throw new SonarException("Failed to read",e);
    }       
    }


    public String getResultsPath()  {
        return  getPieceFromResults(RESULTS_PATTERN);

    }

    public String getCoveragePath() {
        return getPieceFromResults(ATTACHMENTS_PATTERN);
    }
    
    private String getPieceFromResults(String begin)  {
        String msg = "invalid results file " + results + " can 't find ";
        int resultsPos=results.lastIndexOf(begin);
        if(resultsPos==-1) {
            throw new SonarException(msg + begin);
        }
        int captureStart=resultsPos+begin.length();
        while(isWhiteSpace(results.charAt(captureStart))) {
            captureStart++;
        }
        int newLinePos = results.indexOf('\n',captureStart);
        if(newLinePos == -1) {
            throw new SonarException(msg + " end of line");
        }
        String result=results.substring(captureStart,newLinePos);
        return result;
    }


    private boolean isWhiteSpace(char charAt) {
        return charAt == '\t' || charAt == ' ' || charAt == '\r' || charAt == '\n' ;
    }


    public void setResults(String string) {
            results= string;
    }


}
