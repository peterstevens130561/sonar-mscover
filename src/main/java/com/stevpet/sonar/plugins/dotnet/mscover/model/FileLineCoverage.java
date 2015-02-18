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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.util.Log;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class FileLineCoverage {

  private File file;
  private Map<Integer, SourceLine> lines = new HashMap<Integer, SourceLine>();
  private int uncoveredLines = 0; // for efficiency reasons we keep this counter
  private int id;

  /**
   * Constructs a @link{FileCoverage}.
   * @id 
   */
  public FileLineCoverage(int id) {
    this.lines = new HashMap<Integer, SourceLine>();
    this.id = id;
  }


  public FileLineCoverage(int destinationId,
        FileLineCoverage sourceFileLineCoverage) {
      this(destinationId);
      this.file = sourceFileLineCoverage.getFile();
      for(Entry<Integer,SourceLine> entry:sourceFileLineCoverage.getLines().entrySet()) {
          SourceLine line=entry.getValue();
          lines.put(entry.getKey(),line);
          if(line.getVisits()==0) {
              uncoveredLines++;
          }
          
      }
  }


  /**
   * Returns the file, which has a path on the current filesystem: the coveragefile
   * may have paths on other filesystem, but when the file is added to the model it 
   * is converted.
   * @return The file to return.
   */
  public File getFile() {
    return this.file;
  }
  
  /**
   * Set the canonicalFile of the file given.
   * 
   * Sonar stores the canonical names of the files, so we need to present the same. For C# this
   * is already done in the coveragefile, but in C++ the names are lowercase
   * @param file
   */
  public void setCanonicalFile(File file) {
      try {
        this.file=file.getCanonicalFile();  
    } catch (IOException e) {
        String context = "IOException occurred during getting canonicalFile of '" + file.getAbsolutePath() + "'\n";
        Log.error(context + e );
        throw new SonarException(context,e);
    }
  }

  /**
   * Adds a line coverage.
   * 
   * @param lineCoverage
   */
  public void addCoveredLine(CoveragePoint point) {
    int startLine = point.getStartLine();
    SourceLine line = lines.get(startLine);
    if (line == null) {
      line = new SourceLine(startLine);
      lines.put(startLine, line);
    } 
    line.addVisits(1);
  }

  public void addUnCoveredLine(CoveragePoint point) {
      point.setCountVisits(0);
      int startLine = point.getStartLine();
      SourceLine line = lines.get(startLine);
      if (line == null) {
        line = new SourceLine(startLine);
        lines.put(startLine, line);
        uncoveredLines++;
      }
  }


  /**
   * Returns the lines.
   * 
   * @return The lines to return.
   */
  public Map<Integer, SourceLine> getLines() {
    return this.lines;
  }

  /**
   * Returns the countLines.
   * 
   * @return The countLines to return.
   */
  public int getCountLines() {
    return lines.size();
  }

  /**
   * Returns the coveredLines.
   * 
   * @return The coveredLines to return.
   */
  public int getCoveredLines() {
    return lines.size()-uncoveredLines;
  }

  public Object getUncoveredLines() {
      return uncoveredLines;
  }

  /**
   * Gets the coverage ratio.
   * 
   * @return the coverage ratio
   */
  public double getCoverage() {
    if (getCountLines()==0) {
      return 1.;
    }
    double coverage=Math.round(((double) getCoveredLines() / (double) getCountLines()) * 100) * 0.01;
    if(coverage < 0) {
        Log.error("negative coverage on " + this.toString() + " must be programming error");
        coverage=0;
    }
    return coverage;
  } 

  public String toString() {
    return "File(name=" + file.getName() + ", coverage=" + getCoverage() + ", lines=" + getCountLines()
      + ", covered=" + getCoveredLines() + ")";
  }

public int getId() {
    return id;
}


public void merge(FileLineCoverage sourceCoverage) {
    if(sourceCoverage.lines.size() != this.lines.size()) {
        throw new MsCoverException("Can't merge lines, source/dest differ in number of lines " + sourceCoverage.lines.size() + "/" + this.lines.size());  
    }
    for(SourceLine sourceLine:sourceCoverage.lines.values()) {
        int sourceLineNr=sourceLine.getLineNumber();
        SourceLine destLine=lines.get(sourceLineNr);
        int currentVisits= destLine.getVisits();
        
        
        int sourceVisits=sourceLine.getVisits();
        if(lineIsUncoveredButWillBeCovered(sourceVisits, currentVisits)) {
            --uncoveredLines;
        }
        
        destLine.addVisits(sourceVisits);
    }
}


private boolean lineIsUncoveredButWillBeCovered(int sourceVisits,
        int currentVisits) {
    return sourceVisits>0 && currentVisits==0;
}





}
