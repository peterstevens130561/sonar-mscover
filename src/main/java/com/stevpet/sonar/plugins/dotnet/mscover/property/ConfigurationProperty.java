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
package com.stevpet.sonar.plugins.dotnet.mscover.property;

import org.sonar.api.config.PropertyDefinition.Builder;

/**
 * 
 * @author stevpet
 *
 */
public interface ConfigurationProperty<T> {

    /**
     * to be used in he overall Configuration class, which will complete the buil, i.e. add the index
     * {@code new SomeConfiguratonProperty().getBuilder().index(6).build()}
     * @return 
     */
    public Builder getPropertyBuilder() ;
    
    /**
     * get the property value
     * @return 
     */
    public T getValue() ;
    
    /**
     * validate the property value
     * @throws InvalidPropertyValueException
     */
    public void validate();

    /**
     * get the propertykey
     * @return value of the property key
     */
    String getKey();
   
    
}
