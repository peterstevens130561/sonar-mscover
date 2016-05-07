package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class ParserHelper {
    private static Pattern durationPattern = Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d\\.\\d+)");
    public long parseDuration(String duration) {
        Matcher matcher=durationPattern.matcher(duration);
        Preconditions.checkState(matcher.matches(),"duration does not match pattern:" + duration);
        int hours = Integer.parseInt(matcher.group(1));
        int minutes = Integer.parseInt(matcher.group(2));
        long milliSeconds = (long)(Float.parseFloat(matcher.group(3)) * 1E6);
        long durationMilliSeconds = (long)(((hours * 60 ) + minutes)*60E6 + milliSeconds);
        return (long) durationMilliSeconds;
    }
}
