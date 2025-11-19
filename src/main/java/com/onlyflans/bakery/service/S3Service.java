package com.onlyflans.bakery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final String bucketName;
    private final S3Client s3;

    public S3Service(
            @Value("${aws.s3.bucket.name}") String bucketName,
            @Value("${aws.region}") String awsRegion
    ) {
        this.bucketName = bucketName;
        this.s3 = S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    public String uploadFile(MultipartFile file, String productId) throws IOException {

        // Extensión del archivo original
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));

        // Path en S3
        String key = "assets/img/" + productId + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build(); // <-- Sin ACL

        s3.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}