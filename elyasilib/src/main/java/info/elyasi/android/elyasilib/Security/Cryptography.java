package info.elyasi.android.elyasilib.Security;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import info.elyasi.android.elyasilib.Utility.StringExt;


/**
 * Created by Canada on 12/18/2015.
 */
public class Cryptography {
    private static final String TAG = "Security.Cryptography";


    /**
     * 
     * @param pKey in base64 format
     * @param pData in utf-8 format
     * @return HMAC hash value in base64
     */
    public static String HMAC_SHA256(String pKey, String pData) {
        try {
            final byte[] signatureBytes = pData.getBytes("utf-8");
            final byte[] keyBytes = Base64.decode(pKey, Base64.DEFAULT);

            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, hmac.getAlgorithm());
            hmac.init(secretKey);

            final byte[] signatureBytesHmac = hmac.doFinal(signatureBytes);
            String signatureInBase64String = Base64.encodeToString(signatureBytesHmac, Base64.NO_WRAP);
            Log.d(TAG, signatureInBase64String);
            return signatureInBase64String;


        } catch (NoSuchAlgorithmException nex) {
            Log.e(TAG, nex.getMessage());
        } catch (InvalidKeyException iex) {
            Log.e(TAG, iex.getMessage());
        }  catch (UnsupportedEncodingException uex) {
            Log.e(TAG, uex.getMessage());
        }

        return "";
    }

    public static String SHA1_HMAC(String data, String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int blockSize=64; int opad=0x5c; int ipad=0x36;

        // Keys longer than blocksize are shortened
        if (key.length() > blockSize) {
            key = sha1(key, true);
        }

        // Keys shorter than blocksize are right, zero-padded (concatenated)
        key = StringExt.padRight(key, blockSize, (char) 0x00);
        String o_key_pad = "", i_key_pad = "";

        char[] key_char = key.toCharArray();
        for(int i = 0; i < blockSize; i++) {
            int ascii_value = (int)key_char[i];

            char c = (char)(ascii_value ^ opad);
            o_key_pad = o_key_pad + String.valueOf(c);

            c = (char)(ascii_value ^ ipad);
            i_key_pad = i_key_pad + String.valueOf(c);
        }

        return sha1(o_key_pad + sha1(i_key_pad + data), true);
    }

    /*
    public static String base64encoding(String text) {
        byte[] base64 = Base64.encode(text.getBytes(), Base64.NO_WRAP);
        return new String(base64);
    }*/

    public static String md5Base64(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes("utf-8"));

        byte[] md5hash = md.digest();
        return Base64.encodeToString(md5hash, Base64.NO_WRAP);
    }

    public static String base64(String text) throws UnsupportedEncodingException {
        final byte[] bytes = text.getBytes("utf-8");
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static String md5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return md5(text, false);
    }

    public static String md5(String text, boolean raw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes("utf-8"));

        byte[] md5hash = md.digest();
        if (raw)
            return new String(md5hash);
        return convertToHex(md5hash);
    }

    static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return sha1(text, false);
    }

    public static String sha1(String text, boolean raw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //UTF-32LE
        //iso-8859-1
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();

        if (raw)
            return new String(sha1hash);
        return convertToHex(sha1hash);
    }
}
