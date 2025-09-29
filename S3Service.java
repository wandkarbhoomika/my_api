package com.project.api.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadToS3(String bucketName, String key, String content) {
        s3Client.putObject(bucketName, key, new ByteArrayInputStream(content.getBytes()), new ObjectMetadata());
    }
}
