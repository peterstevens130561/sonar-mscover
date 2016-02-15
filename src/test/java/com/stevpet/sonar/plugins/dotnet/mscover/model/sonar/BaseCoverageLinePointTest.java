package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseCoverageLinePointTest {

    @Test
    public void NotEqualAsOtherIsFalse() {
        BaseCoverageLinePoint thisOne = newPoint();
        BaseCoverageLinePoint otherOne = null ;
        assertFalse(thisOne==otherOne);
    }

    @Test
    public void EqualAsIsSame() {
        BaseCoverageLinePoint thisOne = newPoint();
        BaseCoverageLinePoint otherOne = thisOne;
        assertTrue("same instance",thisOne==otherOne);  
    }
    
    @Test
    public void EqualAsAllItemsAreSame() {
        BaseCoverageLinePoint thisOne = newPoint();
        thisOne.setLine(2);

        BaseCoverageLinePoint otherOne = newPoint();
        otherOne.setLine(2);
        assertFalse("line is same",thisOne==otherOne);
    }
    @Test
    public void NotEqualAsLineIsDifferent() {
        BaseCoverageLinePoint thisOne = newPoint();
        thisOne.setLine(2);

        BaseCoverageLinePoint otherOne = newPoint();
        otherOne.setLine(4);
        assertFalse("line is different",thisOne==otherOne);
    }
    
    private BaseCoverageLinePoint newPoint() {
        return new BaseCoverageLinePoint() {

            @Override
            public void merge(CoveragePoint other) {
                // TODO Auto-generated method stub
                
            } };
    }
}
