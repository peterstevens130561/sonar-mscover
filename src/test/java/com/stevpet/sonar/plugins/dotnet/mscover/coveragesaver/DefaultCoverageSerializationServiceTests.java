package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.text.XmlWriter;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class DefaultCoverageSerializationServiceTests {

    XmlWriter xmlWriter ;
    CoverageSerializationService service ;
    private ProjectCoverageRepository repository; 
   
    @Before
    public void before() {
        xmlWriter = mock(XmlWriter.class,RETURNS_DEEP_STUBS);
        service = new DefaultCoverageSerializationService(xmlWriter);
        repository=new DefaultProjectCoverageRepository();
    }
    
    @Test
    public void noCoverage_ShouldHaveBody() {
        File file = null;

        service.Serialize(file, repository);
        verify(xmlWriter,times(1)).begin("coverage");
        verify(xmlWriter,times(1)).end();
        verify(xmlWriter,times(1)).begin(anyString());
    }
}
