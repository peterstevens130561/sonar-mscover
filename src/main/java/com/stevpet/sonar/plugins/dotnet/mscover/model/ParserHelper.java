/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class ParserHelper {
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
