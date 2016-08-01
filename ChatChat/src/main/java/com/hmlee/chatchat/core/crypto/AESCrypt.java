package com.hmlee.chatchat.core.crypto;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * <p><b>The AES(Advanced Encryption Standard) Symmetric Cryptography class.</b></p>
 * Provides cryptographic methods for encrypting or decrypting the data using AES-128.
 *
 * Created by hmlee
 */
public class AESCrypt {

    /** Default IV(Initialization Vector) Seed */
    private static final String DEFAULT_IV_SEED = "_IV@CHATCRYPT15_"; // Must be a 16 letters

    /** Default Key Seed */
    private static final String DEFAULT_KEY_SEED = "$KEY@CHATCRYPT*$"; // Must be a 16 letters

    /** The byte sizes of key */
    private static final int KEY_LENGTH = 16;

    /** Cipher algorithm */
    private static final String ALGORITHM = "AES";

    /** Transformation (cryptographic algorithm/feedback mode/padding scheme) */
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    /**
     * Don't want this class to be instantiated.
     */
    private AESCrypt() {
    }

    /**
     * Encrypt the plain text using the given seed.
     *
     * @param plaintext
     *            the original byte array
     * @return the encrypted text as a base64-encoded string
     * @throws GeneralSecurityException
     */
    public static String encrypt(byte[] plaintext) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(DEFAULT_IV_SEED.getBytes(), 0, KEY_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(DEFAULT_KEY_SEED.getBytes(), 0, KEY_LENGTH, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Encrypt the plain text using the given seed.
     *
     * @param plaintext
     *            the original plaintext
     * @return the encrypted text as a base64-encoded string
     * @throws GeneralSecurityException
     */
    public static String encrypt(String plaintext) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(DEFAULT_IV_SEED.getBytes(), 0, KEY_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(DEFAULT_KEY_SEED.getBytes(), 0, KEY_LENGTH, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Encrypt the plain text using the given IV seed and key seed.
     *
     * @param plaintext
     *            the original plaintext
     * @param ivSeed
     *            the IV seed, the 16-digit strings for generating a IV
     * @param keySeed
     *            the key seed, the 16-digit strings for generating a secret key
     * @return the encrypted text as a base64-encoded string
     * @throws GeneralSecurityException
     */
    public static String encrypt(String plaintext, String ivSeed, String keySeed) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivSeed.getBytes(), 0, KEY_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySeed.getBytes(), 0, KEY_LENGTH, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Encrypt the plain text using the given IV seed and key seed.
     *
     * @param plaintextByte
     *            the original plaintext byte arrays
     * @param ivSeedByte
     *            the IV seed, the 16 byte arrays for generating a IV
     * @param keySeedByte
     *            the key seed, the 16 byte arrays for generating a secret key
     * @return the encrypted text as a base64-encoded string
     * @throws GeneralSecurityException
     */
    public static String encrypt(byte[] plaintextByte, byte[] ivSeedByte, byte[] keySeedByte) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivSeedByte, 0, KEY_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySeedByte, 0, KEY_LENGTH, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plaintextByte);
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Decrypt the encrypted text bytes using the default seed.
     *
     * @param encryptedTextByte
     *            the encrypted text byte arrays
     * @return the original plaintext
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] encryptedTextByte) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(DEFAULT_IV_SEED.getBytes(), 0, KEY_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(DEFAULT_KEY_SEED.getBytes(), 0, KEY_LENGTH, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(encryptedTextByte);
    }

    /**
     * Decrypt the base64-encoded string that be encrypted by AES algorithm using the default seed.
     *
     * @param encodeBase64String
     *            the base64-encoded string
     * @return the original plaintext
     * @throws GeneralSecurityException
     */
    public static String decrypt(String encodeBase64String) throws GeneralSecurityException {
        return new String(decrypt(Base64.decodeBase64(encodeBase64String)));
    }

    /**
     * Decrypt the encrypted text bytes using the given IV seed and key seed.
     *
     * @param encryptedText
     *            the encrypted text byte arrays
     * @param ivSeedByte
     *            the IV seed, the 16 byte arrays for generating a IV
     * @param keySeedByte
     *            the key seed, the 16 byte arrays for generating a secret key
     * @return the original plaintext
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] encryptedText, byte[] ivSeedByte, byte[] keySeedByte) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivSeedByte, 0, KEY_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySeedByte, 0, KEY_LENGTH, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(encryptedText);
    }

}
