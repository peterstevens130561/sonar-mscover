/**
 * 
 */
package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.sonarmeasuresaver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metric.ValueType;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorMock;
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

    private ResourceMediatorMock resourceMediatorMock =  new ResourceMediatorMock();
    private SensorContext sensorContext;
    private MeasureSaver measureSaver;
    private ResourceSeam seam;
    private ResourceSeam nullResourceSeam;
    private ResourceSeam normalSeam;
    private ProjectMock projectMock = new ProjectMock();
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sensorContext = mock(SensorContext.class);  
        org.sonar.api.resources.File dummyResource =  org.sonar.api.resources.File.create("somefile");
        seam = new SonarResourceSeamFactory().createFileResource(sensorContext,dummyResource);
        resourceMediatorMock.givenSonarResource(seam);
        nullResourceSeam = mock(NullResource.class);
        normalSeam = mock(FileResource.class);
        measureSaver = SonarMeasureSaver.create(projectMock.getMock(),sensorContext,resourceMediatorMock.getMock());

    }

    /**
     * @throws java.lang.Exception
     */

    @Test
    public void validResource_shouldSaveMeasure() {
        normalSeam=givenAFileResource();
        Measure measure = new Measure();
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
        verify(normalSeam,times(1)).saveMeasure(eq(measure));
    }


    
    @Test
    public void invalidResource_shouldNotSave() {
        resourceMediatorMock.givenSonarResource(nullResourceSeam);
        Measure measure = new Measure();
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
        verify(nullResourceSeam,times(1)).saveMeasure(eq(measure));
    }
    
    @Test(expected=RuntimeException.class)
    public void duplicateSaveNotIgnored_RuntimeException() {
        givenAResourceThatWillThrowExceptionOnSaveMeasure();
        Measure measure = new Measure();
        saveMeasureOnTheResource(measure);     
    }


    @Test
    public void duplicateSaveValueIgnore_NoException() {
        ResourceSeam exceptionThrowingResource = givenAResourceThatWillThrowExceptionOnSaveMeasure();
        ignoreExceptionOnSavingTheMethodTwice();
        Measure measure = new Measure();
        saveMeasureOnTheResource(measure);    
        verifyThatTheMeasureWasSavedOnTheResource(exceptionThrowingResource, measure);
    }

    @Test
    public void saveMetricValue_ShouldOccur() {
        ResourceSeam resource = givenAFileResource();
        Metric metric = createBogusMetric();
        double value=10.0;
        saveMetricOnTheResource(metric,value);    
        verifyThatTheMetricWasSavedOnTheResource(resource, metric, value);
    }


    
    @Test(expected=RuntimeException.class)
    public void duplicateSaveMetricNotIgnored_RuntimeException() {
        givenAResourceThatWillThrowExceptionOnSaveMeasure();
        Metric metric = createBogusMetric();
        saveMetricOnTheResource(metric,10.0);     
    }

    @Test
    public void duplicateSaveIgnore_NoException() {
        ResourceSeam exceptionThrowingResource = givenAResourceThatWillThrowExceptionOnSaveMeasure();
        ignoreExceptionOnSavingTheMethodTwice();
        Metric metric = createBogusMetric();
        Double value=10.0;
        saveMetricOnTheResource(metric,value);   
        verifyThatTheMetricWasSavedOnTheResource(exceptionThrowingResource, metric,value);
    }
    
    private Metric createBogusMetric() {
        Metric.Builder builder = new Metric.Builder("bogus","bogus",ValueType.FLOAT);
        Metric metric=builder.create();
        return metric;
    }
    
    private void ignoreExceptionOnSavingTheMethodTwice() {
        measureSaver.setIgnoreTwiceSameMeasure();
    }

    private void verifyThatTheMeasureWasSavedOnTheResource(
            ResourceSeam exceptionThrowingResource, Measure measure) {
        verify(exceptionThrowingResource,times(1)).saveMeasure(eq(measure));
    }
    
    private void verifyThatTheMetricWasSavedOnTheResource(
            ResourceSeam resource, Metric measure,Double value) {
        verify(resource,times(1)).saveMetricValue(eq(measure),eq(value));
    }
    
    private void saveMetricOnTheResource(Metric metric,Double value) {
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(metric,value);
    }
    private void saveMeasureOnTheResource(Measure measure) {
        measureSaver.setFile(new File("somefile"));
        measureSaver.saveFileMeasure(measure);
    }

    private ResourceSeam givenAFileResource() {
        ResourceSeam resource = mock(ResourceSeam.class);
        resourceMediatorMock.givenSonarResource(resource);
        return resource;
    }
    private ResourceSeam givenAResourceThatWillThrowExceptionOnSaveMeasure() {
        ResourceSeam exceptionThrowingResource = mock(ResourceSeam.class);
        doThrow(new RuntimeException()).when(exceptionThrowingResource).saveMeasure(any(Measure.class));
        doThrow(new RuntimeException()).when(exceptionThrowingResource).saveMetricValue(any(Metric.class),anyDouble());
        resourceMediatorMock.givenSonarResource(exceptionThrowingResource);
        return exceptionThrowingResource;
    }
   
}
