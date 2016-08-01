package com.hmlee.chatchat.core.crypto;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.hmlee.chatchat.core.util.Generator;

/**
 * Created by hmlee
 */
public class SecureHashHelper {

    /** The minimum number of iteration count */
    private static final int MIN = 1000;

    /** The maximum number of iteration count */
    private static final int MAX = 1200;

    /** The to-be-derived key length */
    private static final int KEY_LENGTH = 1024;

    /** Random Number Generator (RNG) algorithm */
    private static final String SHA1PRNG = "SHA1PRNG";

    /**
     * Don't want this class to be instantiated.
     */
    private SecureHashHelper() {
    }

    /**
     * Generate a secure hash through the PBKDF2.
     * This is based on the PBKDF2 specification using HMAC-SHA1 as the underlying primitive.
     * PBKDF2(Password-Based Key Derivation Function 2) is a key derivation function which conforms to <a href="http://www.ietf.org/rfc/rfc2898.txt">RFC2898</a>.
     *
     * @param protectedText
     *            the protected text such as password
     * @return
     * @throws GeneralSecurityException
     */
    public static String generateSecureHashUsingPBKDF2(String protectedText) throws GeneralSecurityException {
        char[] chars = protectedText.toCharArray();
        byte[] salt = getSalt();
        int iterationCount = Generator.generateRandInt(MIN, MAX);

        PBEKeySpec keySpec = new PBEKeySpec(chars, salt, iterationCount, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = keyFactory.generateSecret(keySpec).getEncoded();
        return iterationCount + ":" + Base64.encodeBase64String(salt) + ":" + Base64.encodeBase64String(hash);
    }

    /**
     * Verify whether the stored hash is equal the hash that be generated from the original text.
     *
     * @param originalText
     *            the original text that inputted
     * @param storedHash
     *            the stored hash
     * @return
     * @throws GeneralSecurityException
     */
    public static boolean validateSecureHashUsingPBKDF2(String originalText, String storedHash)
            throws GeneralSecurityException {
        String[] parts = storedHash.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Incorrectly formatted storedHash: " + storedHash);
        }

        int iterationCount = Integer.parseInt(parts[0]);
        byte[] salt = Base64.decodeBase64(parts[1]);
        byte[] hash = Base64.decodeBase64(parts[2]);

        PBEKeySpec keySpec = new PBEKeySpec(originalText.toCharArray(), salt, iterationCount, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = keyFactory.generateSecret(keySpec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    /**
     * Generate a 32-byte random salt.
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }

}
