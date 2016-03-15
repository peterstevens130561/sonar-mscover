package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModuleSaverExecuteTest {

    
    private static final String COVERAGE_SESSION = "<CoverageSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private OpenCoverModuleParser saver;
    private String moduleName;
    private boolean skipped;

    @Test 
    public void getModuleNameNewOpenCover() {
        String xmlDoc = getXmlDoc("skippedDueTo=\"Filter\"");
        saver=new OpenCoverModuleParser();
        saver.parse(xmlDoc);
        moduleName=saver.getModuleName();
        assertNotNull("should get name",moduleName);
    }

    @Test 
    public void isSkipped() {
        String xmlDoc = getXmlDoc("skippedDueTo=\"Filter\"");
        saver=new OpenCoverModuleParser();
        saver.parse(xmlDoc);
        skipped=saver.getSkipped();
        assertTrue("should be skipped",skipped);
    }
    
    @Test 
    public void isNotSkipped() {
        String xmlDoc = getXmlDoc("");
        saver=new OpenCoverModuleParser();
        saver.parse(xmlDoc);
        skipped=saver.getSkipped();
        assertFalse("should not be skipped",skipped);
    }
    private String getXmlDoc(String attribute) {
        String xmlDoc=XML_VERSION_1_0_ENCODING_UTF_8 + 
                COVERAGE_SESSION+
                "<Modules><Module " + attribute + " hash=\"3F-86-44-9D-0D-07-6A-A0-B0-74-64-5F-BD-19-3E-CE-6E-DE-17-8C\">" +
                "<ModulePath>C:\\windows\\Microsoft.Net\\assembly\\GAC_32\\mscorlib\\v4.0_4.0.0.0__b77a5c561934e089\\mscorlib.dll</ModulePath>" +
                "<ModuleTime>2015-07-31T18:48:30Z</ModuleTime>" +
                "<ModuleName>mscorlib</ModuleName>" +
                "<Classes/>" +
                "</Module></Modules></CoverageSession>";
        return xmlDoc;
    }
}
