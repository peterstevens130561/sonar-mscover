/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.SonarException;

import com.google.common.io.Files;

public class VSTestStdOutParser {

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
