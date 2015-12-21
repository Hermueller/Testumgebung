package at.htl.remotecontrol.entity;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @timeline Text
 * 06.11.2015: MET 060  Class created
 */
public class Cryptography {

    private static final String CIPHERING_METHOD = "AES";
    private static final String CIPHERING_PATH = "AES/CBC/NoPadding";
    private static final String PROVIDER = "SunJCE";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String IV = "AAAAAAAAAAAAAAAA";
    private static final String ENCRYPTION_KEY_DEFAULT = "0123456789HelloWorld";

    /**
     * @param plainText     text which should be encrypted
     * @param encryptionKey coding key
     * @return encrypted text
     */
    public static String encrypt(String plainText, String encryptionKey) {
        if (encryptionKey == null) {
            encryptionKey = ENCRYPTION_KEY_DEFAULT;
            System.out.println("");
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHERING_PATH, PROVIDER);
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(CHARSET_NAME), CIPHERING_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes(CHARSET_NAME)));
            return new String(cipher.doFinal(plainText.getBytes(CHARSET_NAME)));
        } catch (Exception e) {
            System.out.println(plainText + " could not encrypt");
        }
        return plainText;
    }

    /**
     * @param cipherText    encrypted text
     * @param encryptionKey coding key
     * @return decrypted text
     */
    public static String decrypt(String cipherText, String encryptionKey) {
        if (encryptionKey == null) {
            encryptionKey = ENCRYPTION_KEY_DEFAULT;
            System.out.println("");
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHERING_PATH, PROVIDER);
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(CHARSET_NAME), CIPHERING_METHOD);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes(CHARSET_NAME)));
            return new String(cipher.doFinal(cipherText.getBytes(CHARSET_NAME)));
        } catch (Exception e) {
            System.out.println(cipherText + " could not decrypt");
        }
        return cipherText;
    }

}
