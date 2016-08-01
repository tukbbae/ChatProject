package com.hmlee.chatchat.core.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * Created by hmlee
 */
public class Generator {

    /** Random Number Generator (RNG) algorithm */
    public static final String SHA1PRNG = "SHA1PRNG";

    /** The byte sizes of seed */
    private static final int SEED_LENGTH = 16;

    /**
     * Generate a UUID which conforms to <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC4122</a>
     *
     * @return the UUID (universally unique identifier).
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate an identity that be used as database table primary key
     *
     * @return
     */
    public static String generatePrimaryKeyID() {
        return generateUUID().replace("-", "").toUpperCase();
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min
     *            the minimum value
     * @param max
     *            the maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int generateRandInt(int min, int max) {
        if (max - min < 1) {
            throw new IllegalArgumentException("The max must be greater than the min.");
        }

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    /**
     * Generate a fixed length random number.
     *
     * @param digit
     *            the fixed number length
     * @return
     */
    public static String generateRandNumber(int digit) {
        if (digit < 1) {
            return "0";
        }

        if (digit > 18) {
            throw new IllegalArgumentException("The digit is too big. Must be smaller than 18.");
        }

        long min = (long) Math.pow(10, digit - 1);

        long randNumber = (long) (Math.random() * (9 * min)) + min;
        return String.valueOf(randNumber);
    }

    /**
     * Generate a 16-byte random key seed and return the base64 encoded string.
     *
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] generateRandKeySeed() throws GeneralSecurityException {
        SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
        // TzKM에서 AM키 복화시, null문자 포함하면 복호화 실패하여 null문자를 '0'으로 치환하도록 수정함.
        byte[] key = random.generateSeed(SEED_LENGTH);
        for (int i=0; i<key.length; i++) {
            if (key[i] == 0) {
                key[i] = '0';
            }
        }
        return key;
    }

    /**
     * Generate a temp user password
     *
     * @return
     */
    public static String generateUserPassword() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

}
