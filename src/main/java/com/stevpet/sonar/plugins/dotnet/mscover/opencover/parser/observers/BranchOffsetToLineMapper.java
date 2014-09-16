package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

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
        if(sequencePoints == null || sequencePoints.size() == 0) {
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
