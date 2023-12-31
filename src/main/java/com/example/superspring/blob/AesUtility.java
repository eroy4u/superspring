package com.example.superspring.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtility {
    /**
     *
     */
    // public static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    public static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";

    public static class EncryptionParams {
        public String publicKey;
        public String iv;
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static byte[] generateIvBytes() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static EncryptionParams encryptFile(String algorithm, InputStream inputStream, OutputStream outputStream)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKey key = AesUtility.generateKey(256);
        byte[] ivBytes = AesUtility.generateIvBytes();

        encryptFile(algorithm, key, ivBytes, inputStream, outputStream);

        Encoder encoder = Base64.getEncoder();
        EncryptionParams params = new EncryptionParams();
        params.publicKey = encoder.encodeToString(key.getEncoded());
        params.iv = encoder.encodeToString(ivBytes);
        return params;
    }

    public static void encryptFile(String algorithm, SecretKey key, byte[] ivBytes, InputStream inputStream,
            OutputStream outputStream) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        GCMParameterSpec iv = new GCMParameterSpec(128, ivBytes);
        // IvParameterSpec iv = new IvParameterSpec(ivBytes); for non-GCM

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void decryptFile(String algorithm, String base64Key, String base64Iv, InputStream inputStream,
            OutputStream outputStream) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] encodedKey = Base64.getDecoder().decode(base64Key);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        byte[] ivBytes = Base64.getDecoder().decode(base64Iv);

        decryptFile(algorithm, key, ivBytes, inputStream, outputStream);
    }

    public static void decryptFile(String algorithm, SecretKey key, byte[] ivBytes, InputStream inputStream,
            OutputStream outputStream) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        GCMParameterSpec iv = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] output = cipher.doFinal();
        if (output != null) {
            outputStream.write(output);
        }

    }
}
