package com.example.superspring.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import static org.assertj.core.api.Assertions.assertThat;

public class AesUtilityTest {
    @Test
    public void givenFile_whenEncrypt_thenSuccess() throws java.lang.Exception {

        SecretKey key = AesUtility.generateKey(128);
        String algorithm = "AES/CBC/PKCS5Padding";
        byte[] ivBytes = AesUtility.generateIvBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        System.out.println("IV=" + Base64.getEncoder().encodeToString(ivBytes));
        System.out.println("IV2=" + Base64.getEncoder().encodeToString(ivParameterSpec.getIV()));

        Resource resource = new ClassPathResource("aes_utility_test/input.txt");
        File inputFile = resource.getFile();
        File encryptedFile = new File("baeldung.encrypted");
        File decryptedFile = new File("document.decrypted");
        AesUtility.encryptFile(algorithm, key, ivParameterSpec, new FileInputStream(inputFile),
                new FileOutputStream(encryptedFile));
        AesUtility.decryptFile(algorithm, key, ivParameterSpec, new FileInputStream(encryptedFile),
                new FileOutputStream(decryptedFile));

        assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
    }
}
