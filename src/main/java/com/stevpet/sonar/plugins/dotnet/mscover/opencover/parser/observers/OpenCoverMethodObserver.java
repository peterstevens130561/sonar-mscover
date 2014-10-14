package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;









import org.jfree.util.Log;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIdModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

/**
 * observer is used to find the methods that may be used as unit tests.
 * The code is straightforward, ignoring those methods that do not match the patterns. i.e.
 * anonymous methods
 * @author stevpet
 *
 */
public class OpenCoverMethodObserver extends BaseParserObserver {

    private MethodToSourceFileIdMap registry;
    protected String moduleName;
    protected String className;
    protected String fileId ;
    protected String methodName;
    protected String nameSpaceName;
    public OpenCoverMethodObserver() {
        setPattern("(Modules/Module/ModuleName)|" +
                "(Modules/Module/FullName)|" +
                "(Modules/Module/Classes/Class/FullName)|" +
                "(Modules/Module/Classes/Class/Methods/Method/Name)|" +
                "(Modules/Module/Classes/Class/Methods/Method/FileRef)|");

    }

    protected enum ScanMode {
        SCAN,
        SKIP
    }
    
    protected ScanMode scanMode=ScanMode.SCAN ;
    public void setRegistry(MethodToSourceFileIdMap registry) {
        this.registry = registry;
    }
 
    /**
     * The moduleName is like
     *       <ModuleName>BHI.JewelEarth.ThinClient.Common</ModuleName>
     * @param value
     */
    @PathMatcher(path="Modules/Module/FullName")
    public void setModuleName(String value) {
        scanMode=ScanMode.SCAN;
        String regex = ".*\\\\(.*)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value) ;
        if(!matcher.find()) {
            scanMode=ScanMode.SKIP;
            return;
        }
        this.moduleName = matcher.group(1);
    }
    
    /**
     * Derive the namespace name from the classname as below
     * <FullName>BHI.JewelEarth.ThinClient.Common.AsyncPassthroughStream</FullName>
     * the last part is the classname, everything before is the namespace
     * @param value
     */
    @PathMatcher(path="Modules/Module/Classes/Class/FullName")
    public void setNamespaceAndClassName(String value) {
        if("<Module>".equals(value)) {
            return;
        }
        String regex = "(.*)\\.(.*)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value) ;
        if(!matcher.find() || matcher.groupCount() !=2) {
            scanMode=ScanMode.SKIP;
            return;
        }
        this.nameSpaceName = matcher.group(1);
        this.className=matcher.group(2);
                

    }
    
    /**
     * methodname i.e
     * <Name>System.Boolean BHI.JewelEarth.ThinClient.Common.AsyncPassthroughStream::get_CanRead()</Name>
     * @param value
     */
    @ElementMatcher(elementName="Name")
    public void setMethodName(String value){
        String regex = "::(.*)\\(.*\\)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value) ;
        if(!matcher.find() || matcher.groupCount() !=1) {
            scanMode=ScanMode.SKIP;
            return;
        }
        this.methodName = matcher.group(1); 
    }
   


    /**
     * Is last element
     * <FileRef uid="1" />
     * @param value
     */
    @AttributeMatcher(elementName="FileRef",attributeName="uid")
    public void setFileId(String sourceFileId) {
        if(scanMode == ScanMode.SKIP) {
            scanMode=ScanMode.SCAN;
            return;
        }
        MethodIdModel method = new MethodIdModel();
        method.setClassName(className);
        method.setMethodName(methodName);
        method.setModuleName(moduleName);
        method.setNamespaceName(nameSpaceName);
       
        registry.add(method, sourceFileId);
    }
    

}

