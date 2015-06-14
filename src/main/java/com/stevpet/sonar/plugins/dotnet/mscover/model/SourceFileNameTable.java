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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The table provides a means to lookup id by the name {@link getSourceFileId}
 * and name by the id {@link getSourceFileName)
 *
 */
public class SourceFileNameTable  {
    private Map<FileId,SourceFileNameRow> rows = new HashMap<FileId,SourceFileNameRow>();
    private Map<SourceFile,FileId> mapNameToId = new HashMap<SourceFile,FileId>();
    private FileId maxId = new FileId("0");
    /**
     * add a row with given fileID and filePath
     * @param fileID 
     * @param filePath
     */
    public void add(String fileID,String filePath) {
    	SourceFileNameRow row = getNewRow(fileID);
    	row.setSourceFileName(filePath);
    }
    
    public void add(SourceFileNameRow row) {
    	FileId key=row.getSourceFileID();
        rows.put(key,row);
        mapNameToId.put(row.getSourceFile(),key);
        maxId = maxId.compareTo(key)>0?maxId:key;
    }
    
    public SourceFileNameRow getNewRow(String fileId) {
    	SourceFileNameRow row = new SourceFileNameRow().setSourceFileID(fileId);
    	add(row);
    	return row;
    }

    private SourceFileNameRow get(FileId fileId) {
       return rows.get(fileId);
    }

    public SourceFileNameRow get(int index) {
       return rows.get(index);
    }
    public int size() {
        return rows.size();
    }
    
    public Collection<SourceFileNameRow> values() {
        return rows.values();
    }

    /**
     * 
     * @param fileID
     * @return sourcefilename matching fileId, or null if not found
     */
    public SourceFile getSourceFileName(FileId fileID) {
        SourceFileNameRow model = get(fileID);
        if(model==null) {
            return null;
        }
        return model.getSourceFile();
    }
    
    /**
     * makes sure that the file is in the table, returns it's id
     * @param fileName
     * @return
     */
    public FileId getSourceFileId(String fileName) {
        FileId fileId=mapNameToId.get(fileName);
        if(fileId==null) {
            maxId.setNext();
            fileId = (FileId) maxId.clone();
            SourceFileNameRow model = new SourceFileNameRow(fileId,fileName);
            add(model);
        }
        return fileId;
    }


}
  