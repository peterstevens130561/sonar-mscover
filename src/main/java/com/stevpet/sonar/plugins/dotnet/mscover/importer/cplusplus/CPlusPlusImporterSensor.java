package com.stevpet.sonar.plugins.dotnet.mscover.importer.cplusplus;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.DotNetConstants;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver;

//@DependedUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
@DependedUpon("CPlusPlusImporterSensor Executed")
public class CPlusPlusImporterSensor implements Sensor {

    private static final Logger LOG = LoggerFactory
            .getLogger(CPlusPlusImporterSensor.class);
    private final PropertiesHelper propertiesHelper ;
    private final Settings settings;
    private Language language;
    private ModuleFileSystem moduleFileSystem;

    private List<File> cppProjectDirs;
    private List<String> unitTestPaths;
    private SensorContext context;
    private String charsetName;
    private boolean enabled;
    private ResourceFilter filter;

    
    public CPlusPlusImporterSensor(Settings settings,ModuleFileSystem moduleFileSystem) {
        this.settings = settings;
        this.moduleFileSystem = moduleFileSystem;
        this.charsetName=moduleFileSystem.sourceCharset().name();
        propertiesHelper = PropertiesHelper.create(settings);
        filter = ResourceFilterFactory.createUnitTestAssembliesFilter(propertiesHelper);
    }
    public boolean shouldExecuteOnProject(Project project) {
        return propertiesHelper.shouldMsCoverRun();
    }

    public void analyse(Project project, SensorContext context) {
        this.context=context;
        this.enabled= isEnabled();
        cppProjectDirs = new ArrayList<File>();
        unitTestPaths = new ArrayList<String>();
        try {
        findCppProjects(moduleFileSystem.baseDir());
        List<File> sourceDirs = new ArrayList<File>() ;
        sourceDirs.add(moduleFileSystem.baseDir()); 
        
        for(File dir:cppProjectDirs) {
            loadSourcesFromDir(dir,sourceDirs);
        } 
        } catch (Exception e)  {
            throw new SonarException("CPlusPlusImporter terminated with exception " + e.getMessage(),e);
        }
      }

    private void loadSourcesFromDir(File projectDir,List<File> sourceDirs) {

        for (File file : projectDir.listFiles()) {
            if (isCppFile(file)) {
                indexCppSourceFile(file,sourceDirs);
            }
            if(file.isDirectory()) {
                loadSourcesFromDir(file,sourceDirs);
            }
        }
    }
    
    
    private boolean isCppFile(File file) {
        String name=file.getName();
        boolean result=name.endsWith(".h") || name.endsWith(".cpp");
        return result;
    }
    private void indexCppSourceFile(File file,List<File> sourceDirs) {
        
        Resource resource = createResource(file,sourceDirs);
        if (resource != null) {
            try {
                context.index(resource);
                if (enabled) {
                    String source = FileUtils.readFileToString(file,
                            charsetName);
                    context.saveSource(resource, source);
                }
            } catch (Exception e) {
                throw new SonarException(
                        "Unable to read and import the source file : '"
                                + file.getAbsolutePath(),e);
            }
        }
    }

      private void findCppProjects(File root) {
          for(File file:root.listFiles()) {
              if(file.isDirectory()) {
                  findCppProjects(file);
              }
              if(file.getName().endsWith(".dll") && filter.isIncluded(file.getAbsolutePath())) {
                      String directoryPath = file.getParentFile().getParentFile().getAbsolutePath();
                      LOG.debug("Adding unit test directory {}", directoryPath);
                      unitTestPaths.add(directoryPath);
              }
              if( file.getAbsolutePath().endsWith(".vcxproj")) {
                  File directory=file.getParentFile();
                  LOG.debug("Adding source directory {}", directory.getName());
                  cppProjectDirs.add(directory);
              }
          }
      }
      protected Resource createResource(File file,List<File> sourceDirs) {

        org.sonar.api.resources.File resource = org.sonar.api.resources.File.fromIOFile(file, sourceDirs);
        if (resource != null) {
          //resource.setLanguage(language);
          String projectDir=file.getParent();
          if (unitTestPaths.contains(projectDir)) {
            resource.setQualifier(Qualifiers.UNIT_TEST_FILE);
          }
        }
        return resource;
      }

      protected boolean isEnabled() {
        String value=settings.getString(CoreProperties.CORE_IMPORT_SOURCES_PROPERTY);
        return StringUtils.isEmpty(value) || Boolean.parseBoolean(value);
      }

      /**
       * @return the language
       */
      public Language getLanguage() {
        return language;
      }
}
