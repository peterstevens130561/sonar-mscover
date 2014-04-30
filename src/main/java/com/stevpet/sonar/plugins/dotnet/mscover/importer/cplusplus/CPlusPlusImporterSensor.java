package com.stevpet.sonar.plugins.dotnet.mscover.importer.cplusplus;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.DependedUpon;
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

@DependedUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
public class CPlusPlusImporterSensor implements Sensor {

    private final PropertiesHelper propertiesHelper ;
    private final Settings settings;
    private Language language;
    public CPlusPlusImporterSensor(Settings settings) {
        this.settings = settings;
        propertiesHelper = PropertiesHelper.create(settings);
    }
    public boolean shouldExecuteOnProject(Project project) {
        return propertiesHelper.shouldMsCoverRun();
    }

    public void analyse(Project project, SensorContext context) {
    
    }

    protected void analyse(ModuleFileSystem moduleFileSystem, SensorContext context) {
        File file = moduleFileSystem.baseDir();
        FileFilter filter = new CPlusPlusProjectFilter();
        file.listFiles(filter)
        parseDirs(context, moduleFileSystem.sourceDirs(), false, moduleFileSystem.sourceCharset());
      }

      protected void parseDirs(SensorContext context, List<File> sourceDirs, boolean unitTest, Charset sourcesEncoding) {
          List<File> files = new ArrayList<File>();
          boolean enabled=isEnabled();
        for (File file : files) {
          Resource resource = createResource(file, sourceDirs, unitTest);
          if (resource != null) {
            try {
              context.index(resource);
              if (enabled) {
                String source = FileUtils.readFileToString(file, sourcesEncoding.name());
                context.saveSource(resource, source);
              }
            } catch (Exception e) {
              throw new SonarException("Unable to read and import the source file : '" + file.getAbsolutePath() + "' with the charset : '"
                  + sourcesEncoding.name() + "'.", e);
            }
          }
        }
      }

      protected Resource createResource(File file, List<File> sourceDirs, boolean unitTest) {
        org.sonar.api.resources.File resource = org.sonar.api.resources.File.fromIOFile(file, sourceDirs);
        if (resource != null) {
          resource.setLanguage(language);
          if (unitTest) {
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
