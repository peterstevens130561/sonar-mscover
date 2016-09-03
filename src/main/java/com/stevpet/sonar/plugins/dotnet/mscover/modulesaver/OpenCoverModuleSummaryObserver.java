package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.parser.observer.TopLevelObserverRegistrar;

public class OpenCoverModuleSummaryObserver extends  ModuleSummaryObserver{
    private int visitedSequencePoints=0;
    private static final String MODULES_MODULE = "Modules/Module";
    private static final String SUMMARY="Summary";
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules/Module").inElement("Summary")
            .onAttribute("visitedSequencePoints",this::observeVisitedSequencePointsAttribute);
    }

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
