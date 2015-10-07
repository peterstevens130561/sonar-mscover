package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import java.io.File;
import java.util.List;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public interface InspectCodeResultsParser extends BatchExtension {
    /**
     * Parses a processed violation file.
     * 
     * @param file
     *            the file to parse
     */
    List<InspectCodeIssue> parse(File file);

}