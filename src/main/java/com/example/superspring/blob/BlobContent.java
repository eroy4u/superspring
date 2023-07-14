package com.example.superspring.blob;

import java.util.Map;

public class BlobContent {

    private byte[] content;

    private Map<String, String> metadata;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] contentStream) {
        this.content = contentStream;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

}
