package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

import java.util.Set;

public class CoverageHashes {

    private Set<String> hashes = new HashSet<String>(500);
    
    /**
     * register the hash of the string
     * @param coverage
     * @return true if the same string was added before
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public boolean add(String coverage) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest digest=null ;
        digest=MessageDigest.getInstance("MD5");

        String hex="";
        hex = convertByteArrayToHexString(coverage.getBytes("UTF-8"));
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