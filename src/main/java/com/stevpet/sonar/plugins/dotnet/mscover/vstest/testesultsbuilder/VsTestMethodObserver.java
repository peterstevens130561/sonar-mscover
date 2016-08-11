package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;
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
    
    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.onPath(this::moduleNameMatcher,"Module/ModuleName")
        .onPath(this::nameSpaceNameMatcher, "Module/NamespaceTable/NamespaceName")
        .onPath(this::classNameMatcher, "Module/NamespaceTable/Class/ClassName")
        .onPath(this::methodFullNameMatcher, "Module/NamespaceTable/Class/Method/MethodFullName")
        .onPath(this::lineIDMatcher, "Module/NamespaceTable/Class/Method/Lines/SourceFileID");
    }

    public void moduleNameMatcher(String moduleName) {
        this.moduleName = moduleName;
    }

    public void nameSpaceNameMatcher(String nameSpaceName) {
        this.nameSpaceName = nameSpaceName;
    }

    public void classNameMatcher(String className) {
        this.className = className;
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
