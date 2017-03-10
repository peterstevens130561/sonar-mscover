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
package com.stevpet.sonar.plugins.dotnet.mscover.plugin;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeVersion;

@SuppressWarnings({"unchecked","rawtypes","deprecation"})
public class MsCoverPlugingetExtensions {
 
    Logger LOG = LoggerFactory.getLogger(MsCoverPlugingetExtensions.class);
    
    public void allElementsImplementBatchExtension_Pass() {
        Plugin plugin = new MsCoverPlugin() ;
        List<String> allowedInterfaces =Arrays.asList("org.sonar.api.resources.Language","org.sonar.api.batch.Sensor","org.sonar.api.BatchExtension","org.sonar.api.BatchComponent","org.sonar.api.batch.Decorator");


        Plugin.Context context = new Plugin.Context(SonarQubeVersion.V5_6);
        plugin.define(context);
        StringBuilder sb = new StringBuilder();
        for(Object extension:context.getExtensions()) {
            boolean found = checkImplementation(allowedInterfaces, (Class)extension);
            if ( !found) {
                sb.append(((Class)extension).getName()).append(" ");
            }
        }
        String failedPlugins=sb.toString();
        if(StringUtils.isNotEmpty(failedPlugins)) {
            fail("following plugins do not implement an extenstion " + failedPlugins);
        }
    }
    private boolean checkImplementation(List<String> allowedInterfaces,
            Class plugin) {
        LOG.info("checking {}",plugin.getName());
        boolean found = false;
        
        for(Class intf: plugin.getInterfaces()) {
            LOG.info(intf.getName());
            if(allowedInterfaces.contains(intf.getName())) {
                found=true;
            }
        }
        if(!found) {
            Class superClass = plugin.getSuperclass();
              LOG.info(superClass.getName());
              if("java.lang.Object".equals(superClass.getName())) {
                  return false;
              }
              return checkImplementation(allowedInterfaces,plugin.getSuperclass());
        }
        return true;
    }
}
