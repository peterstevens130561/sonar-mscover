/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.property;


import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;


import com.google.common.base.Preconditions;

public abstract class ConfigurationPropertyBase<T> implements ConfigurationProperty<T> {

    private final Settings settings;
    public ConfigurationPropertyBase(Settings settings) {
        this.settings = settings ; 
    }
    public ConfigurationPropertyBase() {
        settings=null;
    }
    public T getValue() {
        Preconditions.checkNotNull(settings);
        return onGetValue(settings);
    }
    
    protected abstract T onGetValue(Settings settings);
    
    public void validate() {
        getValue();
    }
    
    protected Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("Integration tests");

    }
    
    protected  Pattern getPattern(@Nonnull String property){
        String value =settings.getString(property);
        if(StringUtils.isEmpty(value)) {
            return null ;
        }
        Pattern pattern = null ;
        try {
            pattern=Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            String msg = "Property value is not a valid regular expression:" + pattern + "=" + value;
            throw new IllegalStateException(msg,e);
        }
        return pattern;
        
    }

    public ConfigurationPropertyBase register(List<ConfigurationProperty> list) {
        list.add(this);
        return this;
    }

}
