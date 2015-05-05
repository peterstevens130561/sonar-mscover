package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class VsTestMethodObserver extends BaseParserObserver {

    private MethodToSourceFileIdMap methodToSourceFileIdMap;
    private String nameSpaceName;
    private String className;
    private String methodFullName;
    private String moduleName;
    private boolean lookForLine;

    public VsTestMethodObserver() {
        setPattern("(Module/ModuleName)"
                + "|(Module/NamespaceTable/NamespaceName)"
                + "|(Module/NamespaceTable/Class/ClassName)"
                + "|(Module/NamespaceTable/Class/Method/MethodFullName)"
                + "|(Module/NamespaceTable/Class/Method/Lines/SourceFileID)");

    }

    public void setRegistry(MethodToSourceFileIdMap methodToSourceFileIdMap) {
        this.methodToSourceFileIdMap = methodToSourceFileIdMap;

    }

    @PathMatcher(path = "Module/ModuleName")
    public void moduleNameMatcher(String moduleName) {
        this.moduleName = moduleName;
    }

    @PathMatcher(path = "Module/NamespaceTable/NamespaceName")
    public void nameSpaceNameMatcher(String nameSpaceName) {
        this.nameSpaceName = nameSpaceName;
    }

    @PathMatcher(path = "Module/NamespaceTable/Class/ClassName")
    public void classNameMatcher(String className) {
        this.className = className;
    }

    @PathMatcher(path = "Module/NamespaceTable/Class/Method/MethodFullName")
    public void methodFullNameMatcher(String methodSignature) {
        methodFullName = methodSignature;
        lookForLine = true;
    }

    @PathMatcher(path = "Module/NamespaceTable/Class/Method/Lines/SourceFileID")
    public void lineIDMatcher(String sourceFileID) {
        if (lookForLine) {
            lookForLine = false;
            MethodId methodId = new MethodId(moduleName, nameSpaceName,
                    className, methodFullName);
            methodToSourceFileIdMap.add(methodId, sourceFileID);
        }

    }
}
