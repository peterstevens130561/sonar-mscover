package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.List;

import org.sonar.api.BatchExtension;

public interface InspectCodeResultsParser extends BatchExtension {
    /**
     * Parses a processed violation file.
     * 
     * @param file
     *            the file to parse
     */
    List<InspectCodeIssue> parse(File file);

}