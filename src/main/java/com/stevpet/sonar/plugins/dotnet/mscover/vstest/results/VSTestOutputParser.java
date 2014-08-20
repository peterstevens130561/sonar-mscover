package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.SonarException;

import com.google.common.io.Files;

public class VSTestOutputParser {

    private String results;
    private static Pattern RESULTS_PATTERN = Pattern.compile("\\nResults File: (.*\\.trx)");
    private static Pattern ATTACHMENTS_PATTERN = Pattern.compile("\\nAttachments:\\r?\\n  (.*\\.coverage)");

    public void setFile(File resultsFile) {
       try {
        results=Files.toString(resultsFile, Charset.defaultCharset());
    } catch (IOException e) {
        throw new SonarException("Failed to read",e);
    }       
    }


    public String getTestResultsXmlPath()  {
        return  getPieceFromResults(RESULTS_PATTERN);

    }

    public String getCoveragePath() {
        return getPieceFromResults(ATTACHMENTS_PATTERN);
    }
    
    private String getPieceFromResults(Pattern pattern)  {
        Matcher matcher = pattern.matcher(results);
        if(!matcher.find()) {
            throw new SonarException("Could not find area " + pattern.toString());
        }
        String result=matcher.group(1).trim();
        return result;
    }


    public void setResults(String string) {
            results= string;
    }


}
