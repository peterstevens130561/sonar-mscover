/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SonarCoverageSummary implements CoveragePoint {
    private Logger LOG = LoggerFactory.getLogger(SonarCoverageSummary.class);
    private int toCover;
    private int covered;

    /**
     * @return the toCover
     */
    public int getToCover() {
        return toCover;
    }

    /**
     * @return the covered number of lines
     */
    public int getCovered() {
        return covered;
    }

    public void incrementPoint(CoveragePoint point) {
        toCover += point.getToCover();
        covered += point.getCovered();
    }

    /**
     * 
     * @return the coverage ratio ( 1 = 100%)
     */
    public double getCoverage() {
        if (getToCover() == 0) {
            return 1.;
        }
        double coverage = Math
                .round(((double) getCovered() / (double) getToCover()) * 100) * 0.01;
        if (coverage < 0) {
            LOG.error("negative coverage on " + this.toString()
                    + " must be programming error");
            coverage = 0;
        }
        return coverage;
    }
}


