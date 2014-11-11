package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubjectMock;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

public class OpenCoverParserFactoryMock extends GenericClassMock<OpenCoverParserFactory> {
    public OpenCoverParserFactoryMock( ) {
        super(OpenCoverParserFactory.class);
    }

    public void givenXmlParserSubject(XmlParserSubjectMock xmlParserSubjectMock) {
        when(instance.createOpenCoverParser(any(SonarCoverage.class), any(Collection.class))).thenReturn(xmlParserSubjectMock.getMock());
    }
}