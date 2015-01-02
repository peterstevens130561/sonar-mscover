package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;

@SuppressWarnings("deprecation")
public class VSTestCoverageSaver implements CoverageSaver {

    static final Logger LOG = LoggerFactory
            .getLogger(VSTestCoverageSaver.class);
    private final MsCoverProperties propertiesHelper;
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private LineMeasureSaver lineSaver;
    private BlockSaver blockSaver;
    private Project project;
    private String path;
    /**
     * initial instantiation of the helper. Do not forget to invoke
     * setLineSaver & setBlockSaver.
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
    VSTestCoverageSaver(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.propertiesHelper = propertiesHelper ;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }


    public static VSTestCoverageSaver create(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        // TODO Auto-generated method stub
        return new VSTestCoverageSaver(propertiesHelper, microsoftWindowsEnvironment);
    }
    public void setBlockSaver( BlockSaver blockSaver) {
        this.blockSaver = blockSaver;
        
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver#analyse(org.sonar.api.resources.Project, java.lang.String)
     */
    
    public void analyse(Project project, String path) {
        this.project = project;
        this.path = path;

        try {
            tryAnalyse();
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    }

    private void tryAnalyse()
            throws XMLStreamException, IOException {
        LOG.info("MsCoverPlugin : name=" + project.getName());
        String projectDirectory = getCurrentProjectDirectory(project);
        LOG.info("MsCoverPlugin : directory=" + projectDirectory);


        VsTestRegistry registry=new VsTestRegistry(projectDirectory);
   
        invokeParserSubject(registry,path);
        
        saveLineMeasures(registry.getCoverageRegistry());
        
        blockSaver.setSourceFileNamesRegistry(registry.getSourceFileNamesRegistry());
        blockSaver.setFileBlocksRegistry(registry.getFileBlocksRegistry());
        blockSaver.save();
    }


    /**
     * Parse the coverage file, with loading the block coverage, sourcefilenames observers
     * @param fileBlocksRegistry - block coverage
     * @param sourceFileNamesRegistry - sourcefilenames
     * @param path- to coverage file
     * @throws XMLStreamException
     */    
    private void invokeParserSubject(VsTestRegistry registry,String path) throws XMLStreamException {
        VsTestParserFactory parserFactory = new ConcreteVsTestParserFactory();
        XmlParserSubject parserSubject = parserFactory.createCoverageParser(registry);
        File file = getCoverageFile(path);
        parserSubject.parseFile(file);
    }

    private MicrosoftWindowsEnvironment getMicrosoftWindowsEnvironment() {
        return microsoftWindowsEnvironment;
    }

    private File getCoverageFile(String path) {
        LOG.info("MsCover Current working directory :" + microsoftWindowsEnvironment.getWorkingDirectory());
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

    protected String getCurrentProjectDirectory(Project project) {
        String projectName = project.getName();
        VisualStudioProject vsProject = getMicrosoftWindowsEnvironment()
                .getCurrentProject(projectName);
        String projectDirectory ;
        
        if(vsProject == null) {
            ProjectFileSystem fileSystem  = project.getFileSystem();
            try {
                   projectDirectory= fileSystem.getBasedir().getCanonicalPath();
            } catch (IOException e) {
                throw new SonarException(e);
            }
        } else {
            projectDirectory = vsProject.getDirectory().getAbsolutePath();
        }
        LOG.info("projectdir :" + projectDirectory);
        return projectDirectory;
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