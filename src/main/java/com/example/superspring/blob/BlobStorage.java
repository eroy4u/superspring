package com.example.superspring.blob;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface BlobStorage {
  void createContainer(String containerName);

  String generateSasToken(String containerName);

  List<String> listFiles(String containerName);

  BlobContent downloadFile(String containerName, String blob);

  void uploadFile(String container, String blob, InputStream stream, Map<String, String> metadata);
}
