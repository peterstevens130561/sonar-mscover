package com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver;

import static org.junit.Assert.assertEquals;

import java.io.File;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class ResolveOtherFsTest {

    
    @Test
    public void InSameFs_pass() {
        File root = new File("c:/Development/Radiant/Main");
        File toResolve = new File("c:/build/bogus/Radiant/JewelEarth/Solution/Project/File.cs");
        File baseDir= new File("c:/Development/Radiant/Main/JewelEarth/Solution");
        
        File resolved=resolve(toResolve,baseDir,root);
        File expected = new File(baseDir,"Project/File.cs");
        assertEquals("expect to be found",expected,resolved);
    }
    
    @Test
    public void InOtherFs_fail() {
        File root = new File("c:/Development/Radiant/Main");
        File toResolve = new File("c:/build/bogus/Radiant/JewelEarth/Solution/Project/File.cs");
        File baseDir= new File("c:/Development/Radiant/Main/JewelEarth/Core");
        
        File resolved=resolve(toResolve,baseDir,root);
        File expected = null;
        assertEquals("expect to be not found",expected,resolved);
    }
    
    @Test
    public void InSameFsAmbiguous_pass() {
        File root = new File("c:/Development/Radiant/Main");
        File toResolve = new File("c:/build/bogus/Radiant/JewelEarth/Solution/Solution/Project/File.cs");
        File baseDir= new File("c:/Development/Radiant/Main/JewelEarth/Solution");
        
        File resolved=resolve(toResolve,baseDir,root);
        File expected = new File(baseDir,"Solution/Project/File.cs");
        assertEquals("expect to be found",expected,resolved);
    }
    private File resolve(File toResolve,File baseDir,File root) {

        String baseName = baseDir.getName();
        File curDir = new File(toResolve.getAbsolutePath());
        List<String> stack = Lists.newArrayList();
        do {
        while(curDir!=null && !curDir.getName().equalsIgnoreCase(baseName)) {
            stack.add(0,curDir.getName());
            curDir=curDir.getParentFile();

        }
        if(curDir==null) {
            return null;
        }
        File parentFile=curDir.getParentFile();
        if(parentFile==null) {
            return null;
        }
        String curParent=parentFile.getName();
        String baseParent=baseDir.getParentFile().getName();
         if(curParent.equalsIgnoreCase(baseParent)) {
             break;
         }
         stack.add(0,curDir.getName());
         curDir=curDir.getParentFile();
        } while(curDir!=null);
        
        String relativePath = Joiner.on("/").join(stack);
        return new File(baseDir,relativePath);
    }
}
