/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;
public class ShouldExecuteHelperMock extends GenericClassMock<ShouldExecuteHelper> {

    public ShouldExecuteHelperMock() {
        super(ShouldExecuteHelper.class);
    }

    /**
     * when shouldExecuteOnProject is invoked for thisproject, then the result is returned.
     * @param project 
     * @param result
     */
    public void whenShouldExecute(Project project, boolean result) {
        when(instance.shouldExecuteOnProject(project)).thenReturn(result);
    }

}
