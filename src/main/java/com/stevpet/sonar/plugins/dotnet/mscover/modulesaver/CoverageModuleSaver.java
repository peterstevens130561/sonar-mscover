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



interface CoverageModuleSaver  {
    /**
     * The xml doc is stored in directory below coverageRootDir, name of directory is the module in the xml doc
     * and file is named after the testProjectName.
     * <coverageRootDir>/<module>/<testProjectName>.xml
     *
     * @param coverageRootDir - root
     * @param testProjectName - name of the file, use the name of the test project to make sure it is unique
     * @param xml - fully qualified xml doc
     */
    void save(File coverageRootDir, String testProjectName, String xml);

}
