package com.example.superspring.blob;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface BlobStorage {
  void createContainer(String containerName);

  String generateSasToken(String containerName);

  List<ByteArrayOutputStream> downloadFiles(String containerName);

  void uploadFile(String container, String blob, InputStream stream, Map<String, String> metadata);
}
