package com.example.superspring.blob;

import java.util.List;

public interface BlobStorage {
  void createContainer(String containerName);

  String generateSasToken(String containerName);

  List<byte[]> downloadFiles(String containerName);
}
