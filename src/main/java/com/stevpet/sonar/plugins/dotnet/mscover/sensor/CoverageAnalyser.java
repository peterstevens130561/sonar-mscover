package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.UnitTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.CoverageParserListener;
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
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.UnitTestLineSaver;

public class CoverageAnalyser {

    private Project project;
    private SensorContext context;
    private TimeMachine timeMachine;
    private String xmlCoveragePath;
    private PropertiesHelper propertiesHelper;
    


    public CoverageAnalyser(Project project, SensorContext context,TimeMachine timeMachine,PropertiesHelper propertiesHelper) {
        this.project = project;
        this.context = context;  
        this.timeMachine = timeMachine;
        this.propertiesHelper = propertiesHelper;
    }


    public void analyseResults(String xmlCoveragePath) {
        this.xmlCoveragePath = xmlCoveragePath;
        try {
            tryAnalyse();
        } catch (XMLStreamException e) {
            throw new SonarException(e);
        } catch (IOException e) {
            throw new SonarException(e);
        }
    }
    
    private void tryAnalyse()
            throws XMLStreamException, IOException {

        String projectDirectory = getCurrentProjectDirectory(project);
 
        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        sourceFilePathHelper.setProjectPath(projectDirectory);

        CoverageRegistry registry = new FileCoverageRegistry(projectDirectory);
        FileBlocksRegistry fileBlocksRegistry= new FileBlocksRegistry();
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();

        invokeSingleListenerParser(registry);  
        invokeParserSubject(fileBlocksRegistry,sourceFileNamesRegistry);
        
        Saver saver = new UnitTestLineSaver(context, project,registry);
        wireSaver( saver);
        saver.save();
        
        BaseBlockSaver blockSaver = new UnitTestBlockSaver(context, project);
        wireSaver(blockSaver);
        blockSaver.setSourceFilePathHelper(sourceFilePathHelper);
        blockSaver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        blockSaver.setFileBlocksRegistry(fileBlocksRegistry);
        blockSaver.save();
    }


    private void wireSaver(Saver saver) {
        DateFilter dateFilter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        ResourceFilter fileFilter = ResourceFilterFactory.createAntPatternResourceFilter(propertiesHelper);

        saver.setDateFilter(dateFilter);
        saver.setResourceFilter(fileFilter);
    }

    private void invokeSingleListenerParser(CoverageRegistry registry) throws XMLStreamException {
        CoverageParserListener parserListener = new CoverageParserListener();
        parserListener.setRegistry(registry);   
        Parser parser = new SingleListenerParser();
        parser.setListener(parserListener);
        SMInputCursor coverageCursor = getCoverageCursor(xmlCoveragePath);
        parser.parse(coverageCursor);
    }

    private SMInputCursor getCoverageCursor(String path)
            throws XMLStreamException {
        File file = new File(path);
        return getCursor(file);
    }
    /**
     * Parse the coverage file, with loading the block coverage, sourcefilenames observers
     * @param fileBlocksRegistry - block coverage
     * @param sourceFileNamesRegistry - sourcefilenames
     * @param path- to coverage file
     * @throws XMLStreamException
     */
    private void invokeParserSubject(FileBlocksRegistry fileBlocksRegistry, SourceFileNamesRegistry sourceFileNamesRegistry) throws XMLStreamException {
        ParserSubject parserSubject = new CoverageParserSubject();
        
        MethodBlocksObserver methodBlocksObserver = new MethodBlocksObserver();
        methodBlocksObserver.setRegistry(fileBlocksRegistry);
        parserSubject.registerObserver(methodBlocksObserver);
        
        SourceFileNamesObserver sourceFileNamesObserver = new SourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        
        File file = new File(xmlCoveragePath);
        parserSubject.parseFile(file);
    }

    private SMInputCursor getCursor(File file)
            throws FactoryConfigurationError, XMLStreamException {
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
        return rootCursor.advance();
    }

    protected String getCurrentProjectDirectory(Project project) {
        String projectDirectory=null;
        try {
            projectDirectory = project.getFileSystem().getBasedir().getCanonicalPath();
        } catch (IOException e) {
            throw new SonarException("Could not get file",e);
        }
        return projectDirectory;
    }

}
