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
package com.stevpet.sonar.plugins.dotnet.mscover.model;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The table provides a means to lookup id by the name {@link getSourceFileId}
 * and name by the id {@link getSourceFileName)
 *
 */
public class SourceFileNameTable  {
    private Map<Integer,SourceFileNameRow> rows = new HashMap<Integer,SourceFileNameRow>();
    private Map<String,Integer> mapNameToId = new HashMap<String,Integer>();
    private int maxId=0;
    /**
     * add a row with given fileID and filePath
     * @param fileID 
     * @param filePath
     */
    public void add(String fileID,String filePath) {
    	SourceFileNameRow row = getNewRow(fileID);
    	row.setSourceFileName(filePath);
    }
    
    private SourceFileNameRow getNewRow(String fileId) {
        SourceFileNameRow row = new SourceFileNameRow().setSourceFileID(Integer.parseInt(fileId));
        add(row);
        return row;
    }
 
    private void add(SourceFileNameRow row) {
    	int key=row.getSourceFileID();
        rows.put(key,row);
        mapNameToId.put(row.getSourceFileName(),key);
        maxId = maxId>key?maxId:key;
    }
    
    private SourceFileNameRow get(String fileId) {
    	int index = Integer.parseInt(fileId);
       return rows.get(index);
    }


    public Stream<SourceFileNameRow> stream() {
        return rows.values().stream();
    }
    

    /**
     * 
     * @param fileID
     * @return sourcefilename matching fileId, or null if not found
     */
    public String getSourceFileName(String fileID) {
        SourceFileNameRow model = get(fileID);
        if(model==null) {
            return null;
        }
        return model.getSourceFileName();
    }
    



}
  