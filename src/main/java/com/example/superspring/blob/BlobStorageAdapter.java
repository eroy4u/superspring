package com.example.superspring.blob;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.azure.core.util.BinaryData;
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
    public List<byte[]> downloadFiles(String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        return containerClient.listBlobs().stream().map((item) -> {
            return containerClient.getBlobClient(item.getName()).downloadContent().toBytes();
        }).toList();

    }

}
