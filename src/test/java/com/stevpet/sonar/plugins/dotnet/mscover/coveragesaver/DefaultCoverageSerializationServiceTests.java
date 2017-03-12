package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.utils.text.XmlWriter;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.eq;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class DefaultCoverageSerializationServiceTests {

    @Mock XmlWriter xmlWriter ;
    CoverageSerializationService service ;
    private ProjectCoverageRepository repository; 
   
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        when(xmlWriter.begin(anyString())).thenReturn(xmlWriter);
        service = new DefaultCoverageSerializationService(xmlWriter);
        repository=new DefaultProjectCoverageRepository();
    }
    
    @Test
    public void noCoverage_ShouldHaveBody() {
        File file = null;

        service.Serialize(file, repository);
        verify(xmlWriter,times(1)).begin("coverage");
        verify(xmlWriter,times(1)).prop("version","1");
        verify(xmlWriter,times(1)).end();
        verify(xmlWriter,times(1)).begin(anyString());
    }
    
    @Test
    public void oneFileNoResults_ShouldHaveOneFile() {
        File file = null;
        repository.linkFileNameToFileId("myfile", "1");
        service.Serialize(file, repository);
        verify(xmlWriter,times(1)).begin("coverage");
        verify(xmlWriter,times(1)).begin("file");
        verify(xmlWriter,times(1)).prop("path","myfile");
        verify(xmlWriter,times(2)).end();
        
        // no others should be invoked
        verify(xmlWriter,times(2)).begin(anyString());
        verify(xmlWriter,times(2)).prop(anyString(),anyString());
    }
    
    @Test
    public void twoFileNoResults_ShouldHaveTwoFile() {
        File file = null;
        repository.linkFileNameToFileId("myfile", "1");
        repository.linkFileNameToFileId("secondfile", "2");
        service.Serialize(file, repository);
        verify(xmlWriter,times(1)).begin("coverage");
        verify(xmlWriter,times(2)).begin("file");
        verify(xmlWriter,times(1)).prop("path","myfile");
        verify(xmlWriter,times(1)).prop("path","secondfile");
        verify(xmlWriter,times(3)).end();
        
        // no others should be invoked
        verify(xmlWriter,times(3)).begin(anyString());
        verify(xmlWriter,times(3)).prop(anyString(),anyString());
    }
    
    
}
