package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

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
        throw new SonarException(i);
    }
    }
        
    public SonarCoverage deserialize(File sourceFile) {
        try
        {
           FileInputStream fileIn = new FileInputStream(sourceFile.getAbsolutePath());
           ObjectInputStream in = new ObjectInputStream(fileIn);
           SonarCoverage sonarCoverage = (SonarCoverage) in.readObject();
           in.close();
           fileIn.close();
           return sonarCoverage;
        }catch(IOException i)
        {
            throw new SonarException(i);
        }catch(ClassNotFoundException c)
        {
            throw new SonarException(c);
        }
    }
}
