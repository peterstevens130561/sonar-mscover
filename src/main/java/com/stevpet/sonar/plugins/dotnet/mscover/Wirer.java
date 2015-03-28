package com.stevpet.sonar.plugins.dotnet.mscover;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

public class Wirer {
    public  static OpenCoverCoverageParser instantiateOpenCoverCoverageParser(
            VsTestEnvironment vsTestEnvironment,MsCoverProperties msCoverProperties) {
        DefaultPicoContainer parserContainer = new DefaultPicoContainer(new ConstructorInjection());
        parserContainer.addComponent(OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs.class)
        .addComponent(vsTestEnvironment)
        .addComponent(msCoverProperties)
        .addComponent(OpenCoverSequencePointsObserver.class)
        .addComponent(OpenCoverSourceFileNamesObserver.class)
        .addComponent(OpenCoverParserSubject.class)
        .addComponent(OpenCoverCoverageParser.class);
        
        OpenCoverCoverageParser openCoverCoverageParser = parserContainer.getComponent(OpenCoverCoverageParser.class);
        return openCoverCoverageParser;
    }


}
