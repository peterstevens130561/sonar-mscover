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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OpenCoverCommandMock extends GenericClassMock<OpenCoverCommand> {

    public OpenCoverCommandMock() {
        super(OpenCoverCommand.class);
    }
    
    public void verifyOpenSkipAutoProps(boolean expected) {
        int number=expected?1:0;
        verify(instance,times(number)).setSkipAutoProps();
    }

    public void verifySetExcludeFromCodeCoverageAttributeFilter() {
        verify(instance,times(1)).setExcludeFromCodeCoverageAttributeFilter();
    }

    public void verifyFilter(String filter) {
        verify(instance,times(1)).setFilter(filter);     
    }

    public void verifySetTargetDir(String targetDir) {
        verify(instance,times(1)).setTargetDir(targetDir);
    }

}
