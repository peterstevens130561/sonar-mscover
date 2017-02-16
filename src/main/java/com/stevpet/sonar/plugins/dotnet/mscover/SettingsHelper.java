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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;


public class SettingsHelper {
    private Logger LOG = LoggerFactory.getLogger(SettingsHelper.class);
    private Settings settings;

    public SettingsHelper(Settings settings) {
        this.settings=settings ; }
    
    /**
     * get Pattern for the regular expression that the property defines
     * @param property
     * @return pattern
     * @throws IllegalStateException when pattern is invalid
     */
    public Pattern getPattern(@Nonnull String property){
        String value =settings.getString(property);
        if(StringUtils.isEmpty(value)) {
            return null ;
        }
        Pattern pattern = null ;
        try {
            pattern=Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            String msg = "Property value is not a valid regular expression:" + pattern + "=" + value;
            LOG.error(msg);
            throw new IllegalStateException(msg,e);
        }
        return pattern;
        
    }
}
