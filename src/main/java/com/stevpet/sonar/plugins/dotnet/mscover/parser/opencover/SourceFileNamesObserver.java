package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;

public class SourceFileNamesObserver extends BaseParserObserver {

    private SonarCoverage registry ;
    private SonarFileCoverage fileCoverage;
    public SourceFileNamesObserver() {
        setPattern("Modules/Module/Files/File");
    }

    public void setRegistry(SonarCoverage registry) {
        this.registry = registry;
    }
    
    @AttributeMatcher(attributeName = "uid", elementName = "File")
    public void uidMatcher(String attributeValue) {
        fileCoverage = registry.getCoveredFile(attributeValue);
    }
    
    @AttributeMatcher(attributeName="fullPath",elementName="File")
    public void fileMatcher(String attributeValue) {
        fileCoverage.setAbsolutePath(attributeValue);
    }

}
