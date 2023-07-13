package com.example.superspring;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.superspring.blob.AesUtility;
import com.example.superspring.blob.BlobStorage;

@RestController
public class BlobController {
	@Autowired
	private BlobStorage blobContainer;

	@GetMapping("/testCreate")
	public String testCreate() {
		String containerName = java.util.UUID.randomUUID().toString();
		blobContainer.createContainer(containerName);
		return "done";
	}

	@GetMapping("/testGenerate")
	public String testGenerate() {
		String containerName = "6597ba0f-c5e8-44b2-8315-a3570500a18a";
		return blobContainer.generateSasToken(containerName);
	}

	@GetMapping("/testUpload")
	public String testUpload() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, IOException {
		String containerName = "6597ba0f-c5e8-44b2-8315-a3570500a18a";

		FileOutputStream encryptedStream = new FileOutputStream("ABCDE.encrypted.txt");
		String keyAndIv = AesUtility.encryptFile(new FileInputStream("ABCDE.txt"), encryptedStream);
		encryptedStream.close();

		FileInputStream encryptedInput = new FileInputStream("ABCDE.encrypted.txt");
		Map<String, String> metadata = Collections.singletonMap("keyAndIv", keyAndIv);
		blobContainer.uploadFile(containerName, "ABCDE.encrypted.txt", encryptedInput, metadata);
		return "success";
	}

	@GetMapping("/testDownload")
	public String testDownload() {
		String containerName = "6597ba0f-c5e8-44b2-8315-a3570500a18a";
		List<ByteArrayOutputStream> downloadFiles = blobContainer.downloadFiles(containerName);
		if (downloadFiles.size() <= 0) {
			return "no file";
		}
		return new String(downloadFiles.get(0).toByteArray());
	}

	@GetMapping("/testNothing")
	public String testNothing() {
		return "done";
	}

}