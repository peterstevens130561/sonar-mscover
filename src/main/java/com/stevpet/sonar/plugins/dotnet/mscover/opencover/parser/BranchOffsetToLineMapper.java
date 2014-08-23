package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint;

/**
 * Used to map the offset in branchpoints to lines. First load all the sequencepoints in the
 * method through addSequencePoint in the order as in the coverage file, then request the map for each
 * branchpoint in the same order.
 */
public class BranchOffsetToLineMapper {
    List<SequencePoint> sequencePoints;
    public void addSequencePoint(SequencePoint sequencePoint) {
        if(sequencePoint.getOffset()==0) {
            sequencePoints = new ArrayList<SequencePoint>();
        }
        sequencePoints.add(sequencePoint);
    }

    public int mapOffsetToLine(int offset) {
        if(sequencePoints == null || sequencePoints.size() == 0) {
            throw new SonarException("Can't map branch offset to line, as there are no sequence points. Programmer error or corrupted coverage file?");
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
