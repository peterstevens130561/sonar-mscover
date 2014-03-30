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
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.CoverageParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.Parser;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;


public abstract class BaseCoverageSensor implements Sensor {

    static final Logger LOG = LoggerFactory
            .getLogger(BaseCoverageSensor.class);

    protected MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    protected Settings settings;

    private final TimeMachine timeMachine;

    public BaseCoverageSensor(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            TimeMachine timeMachine) {
        this.settings = settings;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.timeMachine = timeMachine;
    }

    /**
     * Creates the coverageSaver to use for the extension
     */
    protected abstract Saver createSaver(Project project,
            SensorContext sensorContext, CoverageRegistry registry);

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
            tryAnalyse(project, sensorContext);
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } 
    }

    private void tryAnalyse(Project project, SensorContext sensorContext)
            throws XMLStreamException {
        LOG.info("MsCoverPlugin : name=" + project.getName());
        String projectDirectory = getCurrentProjectDirectory(project);
        LOG.info("MsCoverPlugin : directory=" + projectDirectory);

        //Create objects
        PropertiesHelper propertiesHelper = new PropertiesHelper(settings);
        Parser parser = new CoverageParser();

        CoverageParserListener parserListener = new CoverageParserListener();
        CoverageRegistry registry = new FileCoverageRegistry(projectDirectory);
        Saver saver = createSaver(project, sensorContext, registry);
        DateFilter dateFilter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        ResourceFilter fileFilter = ResourceFilterFactory.createAntPatternResourceFilter(propertiesHelper);

        saver.setDateFilter(dateFilter);
        saver.setResourceFilter(fileFilter);
        parserListener.setRegistry(registry);
        parser.setListener(parserListener);

        String path = getCoveragePath();
        SMInputCursor coverageCursor = getCoverageCursor(path);
        parser.parse(coverageCursor);
        saver.save();

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
