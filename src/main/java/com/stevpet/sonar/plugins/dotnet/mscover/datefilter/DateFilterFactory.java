package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.sonar.api.batch.TimeMachine;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class DateFilterFactory {
    private DateFilterFactory() {
        
    }
    public static DateFilter createCutOffDateFilter(TimeMachine timeMachine,
            MsCoverProperties helper) {
        String cutOffDate = helper.getCutOffDate();
        DateFilter filter;
        if (cutOffDate == null) {
            filter = new AlwaysPassThroughDateFilter();
        } else {
            filter = new CutOffDateFilter();
        }
        filter.setTimeMachine(timeMachine);
        filter.setCutOffDate(cutOffDate);
        return filter;
    }
    
    public static DateFilter createEmptyDateFilter() {
        return  new AlwaysPassThroughDateFilter();
    }
}
