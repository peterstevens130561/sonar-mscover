package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class VsTestMethodObserver extends BaseParserObserver {

    private MethodToSourceFileIdMap methodToSourceFileIdMap;
    private String nameSpaceName;
    private String className;
    private String methodFullName;
    private String moduleName;
    private boolean lookForLine;

    public void setRegistry(MethodToSourceFileIdMap methodToSourceFileIdMap) {
        this.methodToSourceFileIdMap = methodToSourceFileIdMap;

    }
    
    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.inPath("Module").onElement("ModuleName",value -> this.moduleName=value)
            .inPath("NamespaceTable").onElement("NamespaceName",value-> this.nameSpaceName=value)
                .inPath("Class").onElement("ClassName", value -> this.className = value)
                    .inPath("Method").onElement("MethodFullName",this::methodFullNameMatcher)
                        .inPath("Lines").onElement("SourceFileID",this::lineIDMatcher);

    }


    public void methodFullNameMatcher(String methodSignature) {
        methodFullName = methodSignature;
        lookForLine = true;
    }

    public void lineIDMatcher(String sourceFileID) {
        if (lookForLine) {
            lookForLine = false;
            MethodId methodId = new MethodId(moduleName, nameSpaceName,
                    className, methodFullName);
            methodToSourceFileIdMap.add(methodId, sourceFileID);
        }

    }

}
