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
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;

public class MethodModel extends Model {

    private String module;
    private String method;
    private String fileID;
    private String lnStart;
    
    
    public String getFileID() {
        return fileID;
    }
    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
    public String getModule() {
        return module;
    }
    public String getMethod() {
        return method;
    }
    public String getLnStart() {
        return lnStart;
    }
    
    /**
     * set the pure name of the method, discard anything from the (
     * @param name of method, possibly with the ( etc
     */
    void setMethod(String name) {
        if(!StringUtils.isEmpty(name)) {
            int endIndex = name.indexOf('(');
            if(endIndex>-1) {
                name=name.substring(0, endIndex);
            }
        }
        method=name;
        
    }
    void setLnStart(String name) {
        lnStart=name;
        
    }
     void setModule(String name) {
        module=name;    
    }

    
    public MethodModel createClone()  {
         try {
            return  (MethodModel) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new SonarException(e);
        }
    }
}
