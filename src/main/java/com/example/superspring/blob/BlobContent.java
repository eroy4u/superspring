package com.example.superspring.blob;

import java.util.Map;

public class BlobContent {

    private byte[] contentStream;

    private Map<String, String> metadata;

    public byte[] getContentStream() {
        return contentStream;
    }

    public void setContentStream(byte[] contentStream) {
        this.contentStream = contentStream;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

}
