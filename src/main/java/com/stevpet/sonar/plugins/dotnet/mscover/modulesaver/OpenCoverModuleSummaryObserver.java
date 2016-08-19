package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverPaths;

public class OpenCoverModuleSummaryObserver extends  ModuleSummaryObserver{
    private int visitedSequencePoints=0;
    private static final String MODULES_MODULE = "Modules/Module";
    private static final String SUMMARY="Summary";
    private static final String MODULES_MODULE_SUMMARY = MODULES_MODULE + "/" + SUMMARY;
    
    public OpenCoverModuleSummaryObserver() {
        setPattern(OpenCoverPaths.MODULE_FULLPATH + "|" + MODULES_MODULE_SUMMARY);
    }
    
    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.inPath("Modules/Module/Summary", 
                        (summary -> summary.onAttribute("visitedSequencePoints",this::observeVisitedSequencePointsAttribute)));
    }

    //@AttributeMatcher(elementName = "Summary", attributeName = "visitedSequencePoints") 
    public void observeVisitedSequencePointsAttribute(String value) {
        visitedSequencePoints=Integer.parseInt(value);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.ModuleSummaryObserver#isNotCovered()
     */
    @Override
    public boolean isNotCovered() {
        return visitedSequencePoints == 0 ;
    }



}
