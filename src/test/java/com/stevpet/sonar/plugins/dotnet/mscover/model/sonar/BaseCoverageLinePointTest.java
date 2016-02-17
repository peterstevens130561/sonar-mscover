package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseCoverageLinePointTest {

    @Test
    public void NotEqualAsOtherIsFalse() {
        MyCoverageLinePoint thisOne = newPoint();
        BaseCoverageLinePoint otherOne = null ;
        assertFalse(thisOne==otherOne);
    }

    @Test
    public void EqualAsIsSame() {
        MyCoverageLinePoint thisOne = newPoint();
        MyCoverageLinePoint otherOne = thisOne;
        assertTrue("same instance",thisOne==otherOne);  
    }
    
    @Test
    public void EqualAsAllItemsAreSame() {
        MyCoverageLinePoint thisOne = newPoint();
        thisOne.setLine(2);

        MyCoverageLinePoint otherOne = newPoint();
        otherOne.setLine(2);
        assertFalse("line is same",thisOne==otherOne);
    }
    @Test
    public void NotEqualAsLineIsDifferent() {
        MyCoverageLinePoint thisOne = newPoint();
        thisOne.setLine(2);

        MyCoverageLinePoint otherOne = newPoint();
        otherOne.setLine(4);
        assertFalse("line is different",thisOne==otherOne);
    }
    
    private class MyCoverageLinePoint extends BaseCoverageLinePoint<MyCoverageLinePoint> {

        @Override
        public void merge(MyCoverageLinePoint other) {
            // TODO Auto-generated method stub
            
        }
        
    }
    private MyCoverageLinePoint newPoint() {
        return new MyCoverageLinePoint() ;
}}
