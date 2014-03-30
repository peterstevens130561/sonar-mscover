package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import org.junit.Assert;
import org.sonar.test.TestUtils;

public class Helper {
    public static File getResource(String resourcePath) {
        File resourceFile= TestUtils.getResource(resourcePath);
        Assert.assertTrue("File " + resourcePath + " does not exist",resourceFile.exists());
        return resourceFile;
    }
}
