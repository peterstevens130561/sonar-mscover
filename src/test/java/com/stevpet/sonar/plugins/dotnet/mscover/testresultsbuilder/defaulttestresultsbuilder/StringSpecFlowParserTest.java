package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.codehaus.plexus.util.StringInputStream;
import org.junit.Test;

public class StringSpecFlowParserTest {

    @Test
    public void emptyFile() throws IOException {
        String emptyString="";
        File bogus=new File("bogus");
        StringSpecFlowParser parser = new StringSpecFlowParser();
        Reader inputStream = new StringReader(emptyString);
        SpecFlowScenarioMap map=parser.parse(bogus, inputStream);
        assertEquals("should not have any elements",0,map.size());
    }
    
    @Test
    public void description() throws IOException {
        StringBuilder sb = new StringBuilder() ;
        sb.append("namespace Joa.JewelEarth.Math.Geometry.SpecflowTest.Features\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestMethodAttribute()]\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.DescriptionAttribute(\"Create a plane\")]\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestPropertyAttribute(\"FeatureTitle\", \"Plane\")]\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestCategoryAttribute(\"AcceptanceTest\")]");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestPropertyAttribute(\"VariantName\", \"Variant 0\")]\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestPropertyAttribute(\"Parameter:vector\", \"(1, 0, 0)\")]\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestPropertyAttribute(\"Parameter:D\", \"1\")]\n");
        sb.append("[Microsoft.VisualStudio.TestTools.UnitTesting.TestPropertyAttribute(\"Parameter:normal\", \"(1, 0, 0)\")]\n");
        sb.append("public virtual void CreateAPlane_Variant0()\n");
        File bogus=new File("bogus");
        StringSpecFlowParser parser = new StringSpecFlowParser();
        Reader inputStream = new StringReader(sb.toString());
        SpecFlowScenarioMap map=parser.parse(bogus, inputStream);
        assertEquals("should not have one element",1,map.size());
        SpecFlowScenario scenario = map.get("Joa.JewelEarth.Math.Geometry.SpecflowTest.Features","CreateAPlane_Variant0");
        assertNotNull(scenario);
        assertEquals(bogus,scenario.getFeatureSourceFile());
        assertEquals("Create a plane Variant 0 (1, 0, 0) 1 (1, 0, 0)",scenario.getTestName());
    }
}
