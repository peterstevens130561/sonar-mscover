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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.SequencePoint;

/**
 * Used to map the offset in branchpoints to lines. First load all the sequencepoints in the
 * method through addSequencePoint in the order as in the coverage file, then request the map for each
 * branchpoint in the same order.
 */
public class BranchOffsetToLineMapper {
    private static final Logger LOG = LoggerFactory.getLogger(BranchOffsetToLineMapper.class);
    List<SequencePoint> sequencePoints;
    public void addSequencePoint(SequencePoint sequencePoint) {
        if(sequencePoint.getOffset()==0) {
            sequencePoints = new ArrayList<SequencePoint>();
        }
        sequencePoints.add(sequencePoint);
    }

    /**
     * Map a branchpoint offset to a line. In some cases there are branchpoints without sequence points
     * @param offset
     * @return -1 if no map is possible
     */
    public int mapOffsetToLine(int offset) {
        if(sequencePoints == null || sequencePoints.isEmpty()) {
            LOG.debug("Can't map branch offset to line, as there are no sequence points. Programmer error or corrupted coverage file?");
            return -1;
        }
        for(int pointIndex=0;pointIndex<sequencePoints.size();pointIndex++) {
            SequencePoint currentPoint = sequencePoints.get(pointIndex);
            int currentOffset=currentPoint.getOffset();
            if(currentOffset == offset) {
                return currentPoint.getLine();
            }
            if(currentOffset > offset) {
                SequencePoint previousPoint = getPreviousPoint(pointIndex);
                return previousPoint.getLine();
            }
        }
        int lastIndex=sequencePoints.size()-1;
        SequencePoint lastPoint = sequencePoints.get(lastIndex);
        return lastPoint.getLine();
    }

    private SequencePoint getPreviousPoint(int pointIndex) {
        int previousPointIndex ;
        if(pointIndex==0) { 
            previousPointIndex=0;
        } else {
            previousPointIndex=pointIndex-1;
        }
        SequencePoint previousPoint=sequencePoints.get(previousPointIndex);
        return previousPoint;
    }

    /**
     * wipes out the current list
     */
    public void start() {
        sequencePoints = new ArrayList<SequencePoint>();
    }
}
