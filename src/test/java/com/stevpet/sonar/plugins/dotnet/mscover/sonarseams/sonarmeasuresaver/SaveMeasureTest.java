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
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.FileResource;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.NullResource;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.SonarResourceSeamFactory;
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
    private ResourceSeam seam;
    private ResourceSeam nullResourceSeam;
    private ResourceSeam normalSeam;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sensorContext = mock(SensorContext.class);  
        org.sonar.api.resources.File dummyResource = new org.sonar.api.resources.File("somefile");
        seam = new SonarResourceSeamFactory(sensorContext).createFileResource(dummyResource);
        resourceMediator = mock(ResourceMediator.class);
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(seam);
        nullResourceSeam = mock(NullResource.class);
        normalSeam = mock(FileResource.class);
        measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);

    }

    /**
     * @throws java.lang.Exception
     */

    @Test
    public void validResource_shouldSave() {
        org.sonar.api.resources.File resource=new org.sonar.api.resources.File("somefile");
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(normalSeam);
        Measure measure = new Measure();
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
        verify(normalSeam,times(1)).saveMeasure(eq(measure));
    }
    
    @Test
    public void invalidResource_shouldNotSave() {
        when(resourceMediator.getSonarFileResource(any(File.class))).thenReturn(nullResourceSeam);
        Measure measure = new Measure();
        org.sonar.api.resources.File resource=new org.sonar.api.resources.File("somefile");
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
        verify(nullResourceSeam,times(1)).saveMeasure(eq(measure));
    }
}
