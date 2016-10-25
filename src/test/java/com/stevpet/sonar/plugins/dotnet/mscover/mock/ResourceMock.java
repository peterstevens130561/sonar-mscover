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
package com.stevpet.sonar.plugins.dotnet.mscover.mock;

import org.sonar.api.resources.Resource;

import static org.mockito.Mockito.when;

public class ResourceMock extends GenericClassMock<Resource> {
    public ResourceMock() {
        super(Resource.class);
    }

    /**
     * 
     * @param scope - the string to return when getScope is used
     */
    public void givenScope(String scope) {
        when(instance.getScope()).thenReturn(scope); 
    }

    /**
     *
     * @param longName - the string to return when getLongName is used
     */
    public void givenLongName(String longName) {
        when(instance.getLongName()).thenReturn(longName);
    }
}
