package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
public class CoverageParserMock extends GenericClassMock<CoverageParserStep> {

    public CoverageParserMock() {
        super(CoverageParserStep.class);
    }

    public void thenParse(String path) {

        verify(instance,times(1)).parse(any(SonarCoverage.class), eq(new File(path)));
    }
   

}

