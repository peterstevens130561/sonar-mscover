/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

public class DefaultCoverageFileLocator implements CoverageFileLocator {

        private final  Pattern pattern = Pattern.compile("(.*)\\.(dll|exe)$");
        

        @Override
        public File getFile(File root, String projectName, String assemblyName) {
            Preconditions.checkNotNull(projectName);
            String relativePath=removeSuffix(assemblyName)+ "/" + projectName + ".xml";
            return new File(root,relativePath);
        }
        
        private String removeSuffix(@Nonnull String moduleName) {
            Matcher matcher = pattern.matcher(moduleName);
            String module=matcher.find()?matcher.group(1):moduleName;
            return module;
        }

        @Override
        public File getProjectDir(File root, String assemblyName) {
            return new File(root,assemblyName);
        }
}
