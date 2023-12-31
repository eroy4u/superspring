package com.example.superspring.blob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequestScope
public class BlobStorageAdapter implements BlobStorage {

    @Value("${blob.connection-string}")
    private String connectionString;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void connect() {
        blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        log.info("connected to blob service");
    }

    @Override
    public void createContainer(String containerName) {
        blobServiceClient.createBlobContainer(containerName);
        log.info("create");
    }

    @Override
    public String generateSasToken(String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);

        BlobContainerSasPermission containerSasPermission = new BlobContainerSasPermission().setCreatePermission(true);
        BlobServiceSasSignatureValues serviceSasValues = new BlobServiceSasSignatureValues(expiryTime,
                containerSasPermission);
        return containerClient.generateSas(serviceSasValues);
    }

    @Override
    public List<String> listFiles(String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        return containerClient.listBlobs().stream().map((item) -> item.getName()).toList();

    }

    @Override
    public void uploadFile(String containerName, String blobName, InputStream stream, Map<String, String> metadata) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.upload(stream);
        if (metadata != null && !metadata.isEmpty()) {
            blobClient.setMetadata(metadata);
        }
    }

    @Override
    public BlobContent downloadFile(String containerName, String blobName) {

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        BlobContent content = new BlobContent();

        content.setContent(blobClient.downloadContent().toBytes());
        content.setMetadata(blobClient.getProperties().getMetadata());
        return content;
    }

}
