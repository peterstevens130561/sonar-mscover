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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;

public class OpenCoverModuleSummaryObserver extends  ModuleSummaryObserver{
    private int visitedSequencePoints=0;
    private static final String MODULES_MODULE = "Modules/Module";
    private static final String SUMMARY="Summary";
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules/Module").inElement("Summary")
            .onAttribute("visitedSequencePoints",this::observeVisitedSequencePointsAttribute);
    }

    public void observeVisitedSequencePointsAttribute(String value) {
        visitedSequencePoints=Integer.parseInt(value);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.ModuleSummaryObserver#isNotCovered()
     */
    @Override
    public boolean isNotCovered() {
        return visitedSequencePoints == 0 ;
    }



}
