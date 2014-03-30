package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;


public class AlwaysPassThroughDateFilter implements DateFilter {

    public void setCutOffDate(String date) { 
     // Intentionally left blank
    }

    public void setLineCoverage(Measure lineCoverage) {    
     // Intentionally left blank
    }

    public void setLineCommitDates(Measure lineRevisions) {
     // Intentionally left blank
    }

    /**
     * always true
     */
    public boolean isIncludedInResults() {
        return true;
    }

    public void setTimeMachine(TimeMachine timeMachine) {
        // Intentionally left blank
    }

    public boolean isResourceIncludedInResults(Resource resource) {
        return true;
    }
}
