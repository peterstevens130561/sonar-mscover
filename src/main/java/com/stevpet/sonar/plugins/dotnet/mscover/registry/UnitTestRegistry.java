package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;

public class UnitTestRegistry {
    private UnitTestResultRegistry resultRegistry;
    private ResultsModel summaryResults;
    
    public UnitTestRegistry() {
        resultRegistry = new UnitTestResultRegistry();
        summaryResults = new ResultsModel();
        
    }
    
    /**
     * the summary results
     * @return
     */
    public ResultsModel getSummary() {
        return summaryResults ;
    }
    
    public UnitTestResultRegistry getResults() {
        return resultRegistry;
    }
    
}
