/**
 * 
 */
package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.sonarmeasuresaver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;

/**
 * @author stevpet
 *
 */
public class SaveMeasureTest {

    private ResourceMediator resourceMediator;
    private SensorContext sensorContext;
    private MeasureSaver measureSaver;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        org.sonar.api.resources.File dummyResource = new org.sonar.api.resources.File("somefile");
        resourceMediator = mock(ResourceMediator.class);
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(dummyResource);
        sensorContext = mock(SensorContext.class);  
        
        measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);

    }

    /**
     * @throws java.lang.Exception
     */

    @Test
    public void validResource_shouldSave() {
        org.sonar.api.resources.File resource=new org.sonar.api.resources.File("somefile");
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(resource);
        Measure measure = new Measure();
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
        verify(sensorContext,times(1)).saveMeasure(eq(resource),eq(measure));
    }
    
    @Test
    public void invalidResource_shouldNotSave() {
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(null);
        Measure measure = new Measure();
        org.sonar.api.resources.File resource=new org.sonar.api.resources.File("somefile");
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
        verify(sensorContext,times(0)).saveMeasure(eq(resource),eq(measure));
    }
}
