package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Stopwatch;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;


public class VSTestCoverageSaver implements CoverageSaver {

    static final Logger LOG = LoggerFactory
            .getLogger(VSTestCoverageSaver.class);
    private LineMeasureSaver lineSaver;
    private Project project;
    private FileSystem fileSystem;
    private List<String> artifactNames;
    /**
     * initial instantiation of the helper. Do not forget to invoke
     * setLineSaver 
     * 
     * After this you can invoke it multiple times via analyse
     * 
     * 
     * Note: the constructor takes the items that can be added through IOC, the others are in the
     * plugin
     * 
     * @param propertiesHelper
     * @param microsoftWindowsEnvironment
     * @param timeMachine
     */
    VSTestCoverageSaver(
            FileSystem fileSystem) {
        this.fileSystem=fileSystem;
    }


    public static VSTestCoverageSaver create(
           FileSystem fileSystem) {
        return new VSTestCoverageSaver(fileSystem);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver#analyse
     */
    
    public void analyse(Project project, String path,List<String> artifactNames) {
        this.project = project;
        this.artifactNames = artifactNames;
        try {
            tryAnalyse(path);
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    }

    /*
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver#analyse
     */
    @Override
    public void analyse(Project project, List<File> xmlFiles,
            List<String> artifactNames) {
        this.project = project;
        this.artifactNames = artifactNames;
        try {
            tryAnalyseFiles(xmlFiles);
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    } 

    private void tryAnalyseFiles(List<File> coverageFiles)
            throws XMLStreamException, IOException {
        String projectDirectory = fileSystem.baseDir().getAbsolutePath();
        VsTestRegistry registry=new VsTestRegistry(projectDirectory);
        for(File coverageFile:coverageFiles) {      
            invokeParserSubject(registry,coverageFile);
        }
        
        saveLineMeasures(registry.getCoverageRegistry());
        
    }

    private void tryAnalyse(String coveragePath)
            throws XMLStreamException, IOException {
        LOG.info("MsCoverPlugin : name=" + project.getName());
        String projectDirectory = fileSystem.baseDir().getAbsolutePath();
        LOG.info("MsCoverPlugin : directory=" + projectDirectory);


        VsTestRegistry registry=new VsTestRegistry(projectDirectory);
        File file = getCoverageFile(coveragePath);
        invokeParserSubject(registry,file);
        
        saveLineMeasures(registry.getCoverageRegistry());
        
    }


    /**
     * Parse the coverage file, with loading the block coverage, sourcefilenames observers
     * @param fileBlocksRegistry - block coverage
     * @param sourceFileNamesRegistry - sourcefilenames
     * @throws XMLStreamException
     * @throws IOException 
     */    
    private void invokeParserSubject(VsTestRegistry registry,File coverageFile) throws XMLStreamException {
        VsTestParserFactory parserFactory = new ConcreteVsTestParserFactory();
        XmlParserSubject parserSubject = parserFactory.createCoverageParser(registry,artifactNames);

        Stopwatch sw = new Stopwatch();
        sw.start();
        parserSubject.parseFile(coverageFile);
        LOG.info("----------------------Parsing took {}ms -------------------",sw.elapsedMillis());
    }


    private File getCoverageFile(String path) {
        LOG.info("MsCover Current working directory :" + fileSystem.workDir());
        LOG.info("MsCover coverage file             :" + path); 
        if(StringUtils.isEmpty(path)) {
            throw new SonarException("MsCover: path to coverage file is not defined");
        }
        File file = new File(path);
        if(!file.exists()) {
            throw new SonarException("MsCover : coverage file '" + path + "' does not exist"); 
        }
        return file;
    }

    protected String getCurrentProjectDirectory() {
        return fileSystem.baseDir().getAbsolutePath();
    }

    public void saveLineMeasures(CoverageRegistry registry) {

        for (FileCoverage fileCoverage : registry.getFileCoverages()) {
            File file = fileCoverage.getFile();
            if(file !=null) {
                lineSaver.saveMeasures(fileCoverage, file);
            }
        }

    }

    public void setLineSaver(LineMeasureSaver lineSaver) {
        this.lineSaver=lineSaver; 
    }

}
