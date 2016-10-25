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

import java.util.regex.PatternSyntaxException;

public class InvalidPropertyValueException extends RuntimeException {
    private final String key;
    private final String value;
    public InvalidPropertyValueException(String key, String value,String msg, PatternSyntaxException e) {
        super("Invalid Property value\n" + key + "=" + value +  "\n" + msg,e);
        this.key=key;
        this.value=value;
    }
    public InvalidPropertyValueException(String key, Integer value, String precondition) {
        super("Invalid Property value\n" + key + "=" + value +  "\n" + precondition);
        this.key=key;
        this.value=value.toString();
    }
    
    /**
     * Use to report a property that must be set, but is not
     * @param key
     * @param precondition of the property
     */
    public InvalidPropertyValueException(String key, String precondition) {
        super("Missing property\n" + key + "\n" + precondition);
        this.key=key;
        this.value=null;
    }
    public InvalidPropertyValueException(String key, String value, String precondition) {
        super("Invalid Property value\n" + key + "=" + value +  "\n" + precondition);
        this.key=key;
        this.value=value.toString();
    }
    public String getPropertyKey() {
        return key;
    }
    
    public String getPropertyValue() {
        return value;
    }

}
