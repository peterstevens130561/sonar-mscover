/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.mock;
import static org.mockito.Mockito.mock;

import org.picocontainer.DefaultPicoContainer;

public class GenericClassMock<T> implements ClassMock<T> {
    protected T instance = null;
    private Class clazz;
    
    public GenericClassMock(Class<T> clazz) {
        instance = mock(clazz);
        this.clazz = clazz;
    }
    
   
     /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.mock.ClassMock#getMock()
     */
    public T getMock() {
        return instance;
    }
    
    /**
     * replace the class being mocked in the container by the mock
     * @param container
     */
    public void replace(DefaultPicoContainer container) {
        container.removeComponent(clazz);
        container.addComponent(getMock());
    }

}