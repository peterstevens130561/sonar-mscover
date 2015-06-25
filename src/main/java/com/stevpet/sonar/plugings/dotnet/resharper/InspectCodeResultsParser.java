package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;

import com.stevpet.sonar.plugings.dotnet.resharper.failingissues.IssueListener;

public interface InspectCodeResultsParser {
    /**
     * Parses a processed violation file.
     * 
     * @param file
     *            the file to parse
     */
    abstract void parse(File file);

}