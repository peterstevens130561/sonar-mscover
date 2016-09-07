/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
