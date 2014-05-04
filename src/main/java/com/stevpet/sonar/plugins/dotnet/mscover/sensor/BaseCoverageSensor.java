package com.stevpet.sonar.plugins.dotnet.mscover.sensor;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.CoverageParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.SingleListenerParser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.Parser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodBlocksObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.SourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;

@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
public abstract class BaseCoverageSensor implements Sensor {

    static final Logger LOG = LoggerFactory
            .getLogger(BaseCoverageSensor.class);

    protected MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    protected Settings settings;

    private final TimeMachine timeMachine;

    private PropertiesHelper propertiesHelper;

    public BaseCoverageSensor(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            TimeMachine timeMachine) {
        this.settings = settings;
        this.propertiesHelper = PropertiesHelper.create(settings);
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.timeMachine = timeMachine;
    }

    /**
     * Creates the coverageSaver to use for the extension
     */
    protected abstract Saver createLineSaver(Project project,
            SensorContext sensorContext, CoverageRegistry registry);

    protected abstract BaseBlockSaver createBlockSaver(Project project, SensorContext sensorContext);
    /**
     * Gets the path to the coverage file for the extension
     * 
     * @return
     */
    protected abstract String getCoveragePath();

    /**
     * Determines whether to execute the sensor or not
     */
    protected abstract boolean shouldExecuteSensor(PropertiesHelper helper);

    /**
     * invoked by Sonar to determine whether the sensor should be executed or
     * not
     */
    public boolean shouldExecuteOnProject(Project project) {
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        if (project == null) {
            LOG.error("MSCover : project is null, will not execute");
            return false;
            
        }
        PropertiesHelper helper = new PropertiesHelper(settings);
        
        if(project.isRoot() != helper.excuteRoot()) {
            LOG.info("MSCover : Skipping project project.isRoot() =" + project.isRoot() + ", " + PropertiesHelper.MSCOVER_EXECUTEROOT + "=" + helper.excuteRoot());
            return false;
        }

        return shouldExecuteSensor(helper);
    }

    /**
     * performs the analysis
     */
    public void analyse(Project project, SensorContext sensorContext) {
        try {
            PropertiesHelper propertiesHelper = new PropertiesHelper(settings);
            CoverageAnalyser coverageAnalyser = new CoverageAnalyser(project,sensorContext, timeMachine, propertiesHelper);
            tryAnalyse(project, sensorContext);
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    }

    private void tryAnalyse(Project project, SensorContext sensorContext)
            throws XMLStreamException, IOException {
        LOG.info("MsCoverPlugin : name=" + project.getName());
        String projectDirectory = getCurrentProjectDirectory(project);
        LOG.info("MsCoverPlugin : directory=" + projectDirectory);
        String sourcedirs = settings.getString("sonar.sources");
        LOG.info("MsCoverPlugin : sources=" + sourcedirs);
        List<File> dirs = project.getFileSystem().getSourceDirs();
        if(dirs==null) {
            LOG.info("no sourcedirs");
        }
        //Create objects
        PropertiesHelper propertiesHelper = new PropertiesHelper(settings);
        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        sourceFilePathHelper.setProjectPath(projectDirectory);

        CoverageRegistry registry = new FileCoverageRegistry(projectDirectory);
        FileBlocksRegistry fileBlocksRegistry= new FileBlocksRegistry();
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();

        String path = getCoveragePath();
        
        invokeSingleListenerParser(registry, path);  
        invokeParserSubject(fileBlocksRegistry,sourceFileNamesRegistry,path);
        
        Saver saver = createLineSaver(project, sensorContext, registry);
        wireSaver(propertiesHelper, saver);
        saver.save();
        
        BaseBlockSaver blockSaver = createBlockSaver(project,sensorContext);
        wireSaver(propertiesHelper,blockSaver);
        blockSaver.setSourceFilePathHelper(sourceFilePathHelper);
        blockSaver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        blockSaver.setFileBlocksRegistry(fileBlocksRegistry);
        blockSaver.save();
    }


    private void wireSaver(PropertiesHelper propertiesHelper, Saver saver) {
        DateFilter dateFilter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        ResourceFilter fileFilter = ResourceFilterFactory.createAntPatternResourceFilter(propertiesHelper);

        saver.setDateFilter(dateFilter);
        saver.setResourceFilter(fileFilter);
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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
