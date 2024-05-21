package com.msdk.xsdk.utils;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class XAES {

    public static String B2SrclYHQH3aCQJL(String... data) {
        try {
            String ZAF95ZJRQB4PC="";
            Cipher cipher = cZ7d5vn3L2Hvda();
            for (String str:data
                 ) {
                byte[] LTuFCUrh2uzAdq = getBytes(cipher, str);
                ZAF95ZJRQB4PC+=new String(LTuFCUrh2uzAdq, StandardCharsets.UTF_8);
            }
            return ZAF95ZJRQB4PC;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
    public static Cipher cZ7d5vn3L2Hvda() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, XException {
        Cipher cipher = irGZ();
        cipher.init(Cipher.DECRYPT_MODE,Cgycewfww(CBWHYIEUW()));
        return cipher;
    }

    private static byte[] CBWHYIEUW() throws XException {
        byte[] keyBytes = "1234567887654321".getBytes(StandardCharsets.UTF_8);
        return keyBytes;
    }

    private static Cipher irGZ() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        return cipher;
    }

    @NonNull
    private static SecretKeySpec Cgycewfww(byte[] keyBytes) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes,"AES");
        return secretKeySpec;
    }
    private static byte[] getBytes(Cipher cipher, String str) throws BadPaddingException, IllegalBlockSizeException {
        byte[] QZhsDqkbfn0OpJqpPX6al = android.util.Base64.decode(str, android.util.Base64.DEFAULT);
        byte[] LTuFCUrh2uzAdq = cipher.doFinal(QZhsDqkbfn0OpJqpPX6al);
        return LTuFCUrh2uzAdq;
    }


}
