package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class ParserHelper {
    private static Pattern durationPattern = Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d\\.\\d+)");
    private static Pattern timePattern = Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d+)");

    
    /**
     * Parses the duration of the test into a localtime instance. Max 23:59:59.99999
     * @param duration
     * @return
     */
    public LocalTime parseDurationToTime(String duration) {
        Matcher matcher=timePattern.matcher(duration);
        Preconditions.checkState(matcher.matches(),"duration does not match pattern:" + duration);
        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));
        int second = Integer.parseInt(matcher.group(3));
        int nanoOfSecond = (int) (Float.parseFloat("0." + matcher.group(4))*1E9);
        return LocalTime.of(hour, minute, second, nanoOfSecond);
    }
}
