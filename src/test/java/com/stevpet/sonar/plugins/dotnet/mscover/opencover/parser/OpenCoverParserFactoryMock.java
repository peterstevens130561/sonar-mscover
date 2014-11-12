package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubjectMock;

public class OpenCoverParserFactoryMock extends GenericClassMock<OpenCoverParserFactory> {
    public OpenCoverParserFactoryMock( ) {
        super(OpenCoverParserFactory.class);
    }

    public void givenXmlParserSubject(XmlParserSubjectMock xmlParserSubjectMock) {
        when(instance.createOpenCoverParser(any(SonarCoverage.class), any(MsCoverProperties.class))).thenReturn(xmlParserSubjectMock.getMock());
    }
}