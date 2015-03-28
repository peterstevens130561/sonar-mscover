package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.io.File;
import java.util.Collection;

import org.picocontainer.annotations.Inject;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

public class OpenCoverCoverageParser{

    @Inject
    private MsCoverProperties msCoverProperties;
    @Inject
    private XmlParserSubject parser ; 
    @Inject
    private OpenCoverObserver[] observers;
    private VsTestEnvironment vsTestEnvironment;
    
    public OpenCoverCoverageParser(MsCoverProperties msCoverProperties,
            XmlParserSubject parser,OpenCoverObserver[] observers,
            VsTestEnvironment vsTestEnvironment
            ) {
        this.msCoverProperties=msCoverProperties;
        this.parser=parser;
        this.observers=observers;
        this.vsTestEnvironment = vsTestEnvironment;
    }
    
    public void execute() {
        File file= new File(vsTestEnvironment.getXmlCoveragePath());
        for(OpenCoverObserver observer: observers) {
            parser.registerObserver(observer);
        }
        parser.parseFile(file);
    }
}
