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