package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class XmlParserSubjectMock extends GenericClassMock<XmlParserSubject> {
    public XmlParserSubjectMock() {
        super(XmlParserSubject.class);
    }

    public void verifyParseFile(String string) {
        verify(instance,times(1)).parseFile(new File(string));
    }
}
