package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;

public class OpenCoverSourceFileNamesObserver extends OpenCoverObserver {

    private SonarFileCoverage fileCoverage;
    public OpenCoverSourceFileNamesObserver() {
        setPattern("Modules/Module/Files/File");
    }

    @AttributeMatcher(attributeName = "uid", elementName = "File")
    public void uidMatcher(String attributeValue) {
        fileCoverage = getRegistry().getCoveredFile(attributeValue);
    }
    
    @AttributeMatcher(attributeName="fullPath",elementName="File")
    public void fileMatcher(String attributeValue) {
        fileCoverage.setAbsolutePath(attributeValue);
    }

}
