package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

public class DefaultCoverageFileLocator implements CoverageFileLocator {

        private final  Pattern pattern = Pattern.compile("(.*)\\.(dll|exe)$");
        

        @Override
        public File getArtifactCoverageFile(File root, String projectName, String artifactName) {
            Preconditions.checkNotNull(projectName);
            String relativePath=removeSuffix(artifactName)+ "/" + projectName + ".xml";
            return new File(root,relativePath);
        }
        
        private String removeSuffix(@Nonnull String moduleName) {
            Matcher matcher = pattern.matcher(moduleName);
            String module=matcher.find()?matcher.group(1):moduleName;
            return module;
        }
}
