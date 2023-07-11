package com.example.superspring;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/testDownload")
	public String testDownload() {
		String containerName = "6597ba0f-c5e8-44b2-8315-a3570500a18a";
		List<byte[]> downloadFiles = blobContainer.downloadFiles(containerName);
		if (downloadFiles.size() <= 0) {
			return "no file";
		}
		return Base64.getEncoder().encodeToString(downloadFiles.get(0));
	}

	@GetMapping("/testNothing")
	public String testNothing() {
		return "done";
	}

}