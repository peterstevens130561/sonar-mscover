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

package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class SourceLine {

  private final int lineNumber;
  private int visits = 0;

  /**
   * Constructs a @link{FileLine}.
   */
  public SourceLine(int lineNumber) {
    this.lineNumber = lineNumber;
  }


  /**
   * Returns the lineNumber.
   * 
   * @return The lineNumber to return.
   */
  public int getLineNumber() {
    return this.lineNumber;
  }

  /**
   * Returns the countVisits.
   * 
   * @return The countVisits to return.
   */
  public int getVisits() {
    return this.visits;
  }

public void addVisits(int visits) {
    this.visits += visits;
}
}
