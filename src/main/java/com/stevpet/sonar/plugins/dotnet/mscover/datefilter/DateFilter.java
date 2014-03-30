package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

public interface DateFilter {

    void setCutOffDate(String date);

    void setLineCoverage(Measure lineCoverage);
    

    void setLineCommitDates(Measure lineRevisions);

    boolean isIncludedInResults();

    void setTimeMachine(TimeMachine timeMachine);

    boolean isResourceIncludedInResults(Resource resource);

}
