package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.CoverageParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.Parser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.SingleListenerParser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodBlocksObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.SourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver;

public class CoverageHelper {

    static final Logger LOG = LoggerFactory
            .getLogger(CoverageHelper.class);
    private final PropertiesHelper propertiesHelper;
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private LineMeasureSaver lineSaver;
    private BlockSaver blockSaver;
    private Project project;
    private SensorContext sensorContext;
    private String path;
    private ResourceMediator resourceMediator;

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
    private CoverageHelper(PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.propertiesHelper = propertiesHelper ;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    public static CoverageHelper create(PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        return new CoverageHelper(propertiesHelper,microsoftWindowsEnvironment);
    }

    public void setLineSaver(LineMeasureSaver lineSaver) {
        this.lineSaver = lineSaver;    
    }

    public boolean shouldExecuteOnProject(Project project) {
        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        if (project == null) {
            LOG.error("MSCover : project is null, will not execute");
            return false;
            
        }
        if(project.isRoot() != propertiesHelper.excuteRoot()) {
            LOG.info("MSCover : Skipping project project.isRoot() =" + project.isRoot() + ", " + PropertiesHelper.MSCOVER_EXECUTEROOT + "=" + propertiesHelper.excuteRoot());
            return false;
        }
        return true;
    }

    public void setBlockSaver( BlockSaver blockSaver) {
        this.blockSaver = blockSaver;
        
    }

    /**
     * performs the analysis
     */
    public void analyse(Project project, SensorContext sensorContext,String path,ResourceMediator resourceMediator) {
        this.project = project;
        this.sensorContext = sensorContext;
        this.path = path;
        this.resourceMediator=resourceMediator;

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


        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        sourceFilePathHelper.setProjectPath(projectDirectory);

        CoverageRegistry registry = new FileCoverageRegistry(projectDirectory);
        FileBlocksRegistry fileBlocksRegistry= new FileBlocksRegistry();
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();

        invokeSingleListenerParser(registry, path);  
        invokeParserSubject(fileBlocksRegistry,sourceFileNamesRegistry,path);
        
        saveLineMeasures(registry);
        
        blockSaver.setSourceFilePathHelper(sourceFilePathHelper);
        blockSaver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        blockSaver.setFileBlocksRegistry(fileBlocksRegistry);
        blockSaver.save();
    }



    private void invokeSingleListenerParser(CoverageRegistry registry,
            String path) throws XMLStreamException {
        CoverageParserListener parserListener = new CoverageParserListener();
        parserListener.setRegistry(registry);   
        Parser parser = new SingleListenerParser();
        parser.setListener(parserListener);
        SMInputCursor coverageCursor = getCoverageCursor(path);
        parser.parse(coverageCursor);
    }

    /**
     * Parse the coverage file, with loading the block coverage, sourcefilenames observers
     * @param fileBlocksRegistry - block coverage
     * @param sourceFileNamesRegistry - sourcefilenames
     * @param path- to coverage file
     * @throws XMLStreamException
     */
    private void invokeParserSubject(FileBlocksRegistry fileBlocksRegistry, SourceFileNamesRegistry sourceFileNamesRegistry,String path) throws XMLStreamException {
        ParserSubject parserSubject = new CoverageParserSubject();
        
        MethodBlocksObserver methodBlocksObserver = new MethodBlocksObserver();
        methodBlocksObserver.setRegistry(fileBlocksRegistry);
        parserSubject.registerObserver(methodBlocksObserver);
        
        SourceFileNamesObserver sourceFileNamesObserver = new SourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        
        File file = getCoverageFile(path);
        parserSubject.parseFile(file);
    }

    private MicrosoftWindowsEnvironment getMicrosoftWindowsEnvironment() {
        return microsoftWindowsEnvironment;
    }

    private SMInputCursor getCoverageCursor(String path)
            throws XMLStreamException {
        File file = getCoverageFile(path);
        return getCursor(file);
    }

    private SMInputCursor getCursor(File file)
            throws FactoryConfigurationError, XMLStreamException {
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
        return rootCursor.advance();
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
            lineSaver.saveMeasures(fileCoverage, file);
        }

    }
    
    public org.sonar.api.resources.File getSonarFile(FileCoverage fileCoverage) {
        File file = fileCoverage.getFile();
        if (file == null) {
            return null;
        }
        return resourceMediator.getSonarFileResource(file);
    }
}
