package org.mili.multisim.util;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author
 */
public class Crypt {

    public static String sha1(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] encryptedMessage = md.digest(source.getBytes("UTF-8"));
            return new String(Hex.encodeHex(encryptedMessage));
        } catch (NoSuchAlgorithmException nse) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] md5(String source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(source.getBytes());
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }

}
