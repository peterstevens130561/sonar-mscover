/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.sonar.api.BatchExtension;

public class CoverageHashes implements BatchExtension{

    private Set<String> hashes = Collections.synchronizedSet(new HashSet<String>(500));
    
    /**
     * register the hash of the string
     * @param coverage
     * @return true if the same string was added before
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public boolean add(String coverage) {
        MessageDigest digest=null ;
        try {
            digest=MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        String hex="";
        try {
            hex = convertByteArrayToHexString(coverage.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        digest.reset();
        digest.update(hex.getBytes());
        byte[] hash=digest.digest();
        String hashString = convertByteArrayToHexString(hash);
        boolean result=hashes.contains(hashString);
        if(!result) {
            hashes.add(hashString);
        }   
        return result;
    }
    
private static String convertByteArrayToHexString(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer(3000000);
    for (int i = 0; i < bytes.length; i++) {
        stringBuffer.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                .substring(1));
    }
    return stringBuffer.toString();
}

}