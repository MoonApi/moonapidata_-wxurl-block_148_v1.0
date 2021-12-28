package com.moonapi.demo;

import com.sun.istack.internal.Nullable;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.util.*;

class MoonapiSign {
    private final static String ALGORITHM = "UTF-8";

    public static String generateHmacSHA1Signature(Map<String, String> parameters,
                                                   String accessKeySecret) throws Exception {
        String signString = generateSignString(parameters, accessKeySecret);
        byte[] signBytes = hmacSHA1Signature(accessKeySecret, signString);

        assert signBytes != null;
        return byteToHex(signBytes);
    }

    public static String generateMd5Signature(Map<String, String> parameters,
                                              String accessKeySecret) throws Exception {
        String signString = generateSignString(parameters, accessKeySecret);
        byte[] signBytes = md5Signature(accessKeySecret, signString);

        assert signBytes != null;
        return byteToHex(signBytes);
    }

    public static String generateSignString(Map<String, String> parameters,
                                            String accessKeySecret) {

        ArrayList<String> arrString = new ArrayList<String>();

        Set<String> keySet = parameters.keySet();

        for (String key : keySet) {
            String value = parameters.get(key);

            arrString.add(key+"="+value);
        }

        Collections.sort(arrString);

        return String.join("&", arrString)+":"+accessKeySecret;
    }

    public static String getUrlQueryFromParams(Map<String, String> parameters){
        ArrayList<String> arrString = new ArrayList<String>();

        Set<String> keySet = parameters.keySet();

        for (String key : keySet) {
            String value = parameters.get(key);

            arrString.add(key+"="+value);
        }

        return String.join("&", arrString);
    }

    public static byte[] hmacSHA1Signature(String secret, String baseString)
            throws Exception {
        if (isEmpty(secret)) {
            throw new IOException("secret can not be empty");
        }
        if (isEmpty(baseString)) {
            return null;
        }
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        mac.init(keySpec);
        return mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] md5Signature(String secret, String baseString)
            throws Exception {
        if (isEmpty(secret)) {
            throw new IOException("secret can not be empty");
        }
        if (isEmpty(baseString)) {
            return null;
        }

        MessageDigest md5=MessageDigest.getInstance("MD5");

        return md5.digest(baseString.getBytes(StandardCharsets.UTF_8));
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }
}
