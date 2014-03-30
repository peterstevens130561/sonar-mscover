/*
 * Sonar .NET Plugin :: MsCover
 * Copyright (C) 2014 Peter Stevens
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.listener;

import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.in.SMInputCursor;

public interface ParserListener {
    /**
     * deal with a line element
     * 
     * @param linesCursor point to the start of the element
     * @throws Exception 
     * @throws XMLStreamException 
     */
    void onLine(SMInputCursor linesCursor) throws XMLStreamException;
    /**
     * deal with SourceFileNames element
     * 
     * @param childCursor point to the start of the element
     * @throws XMLStreamException 
     */
    void onSourceFileNames(SMInputCursor childCursor) throws XMLStreamException;
    /**
     * Deal with a module. 
     * @param moduleName
     * @return true to continue processing the module
     */
    boolean  onModuleName(String moduleName);
}
