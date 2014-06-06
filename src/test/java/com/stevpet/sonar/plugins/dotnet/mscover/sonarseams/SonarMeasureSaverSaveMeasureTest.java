/**
 * 
 */
package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;

/**
 * @author stevpet
 *
 */
public class SonarMeasureSaverSaveMeasureTest {

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
        measureSaver.setFile(new File("somefile"));
    }

    /**
     * @throws java.lang.Exception
     */

    @Test
    public void validResource_shouldSave() {
        org.sonar.api.resources.File resource=new org.sonar.api.resources.File("somefile");
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(resource);
        Measure measure = new Measure();
        measureSaver.saveMeasure(measure);
        verify(sensorContext,times(1)).saveMeasure(eq(resource),eq(measure));
    }
    
    @Test
    public void invalidResource_shouldNotSave() {
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(null);
        Measure measure = new Measure();
        measureSaver.saveMeasure(measure);
        verify(sensorContext,times(0)).saveMeasure(eq(measure));
    }

}
