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
package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class Serializer {
    public void serialize(File destinationFile,Object object) {
        try
    {
       FileOutputStream fileOut =new FileOutputStream(destinationFile.getAbsolutePath());
       ObjectOutputStream out = new ObjectOutputStream(fileOut);
       out.writeObject(object);
       out.close();
       fileOut.close();
    } catch(IOException i)
    {
        throw new IllegalStateException(i);
    }
    }
        
    public ProjectCoverageRepository deserialize(File sourceFile) {
        try
        {
           FileInputStream fileIn = new FileInputStream(sourceFile.getAbsolutePath());
           ObjectInputStream in = new ObjectInputStream(fileIn);
           ProjectCoverageRepository sonarCoverage = (ProjectCoverageRepository) in.readObject();
           in.close();
           fileIn.close();
           return sonarCoverage;
        }catch(IOException i)
        {
            throw new IllegalStateException(i);
        }catch(ClassNotFoundException c)
        {
            throw new IllegalStateException(c);
        }
    }
}
