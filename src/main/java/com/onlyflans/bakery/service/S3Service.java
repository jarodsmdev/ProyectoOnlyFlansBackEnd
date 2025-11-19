package com.onlyflans.bakery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

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

    private static final List<String> ALLOWED_MIME = List.of(
            "image/png",
            "image/jpeg",
            "image/webp"
    );

    private static final List<String> ALLOWED_EXT = List.of(
            ".png", ".jpg", ".jpeg", ".webp"
    );

    public String uploadFile(MultipartFile file, String productId) throws IOException {

        // Validar el archivo, el método arrojará un exception si no es válido
        validateFile(file);

        // Extensión del archivo original
        String original = file.getOriginalFilename();
        String ext = original.substring(original.lastIndexOf(".")).toLowerCase();

        // Path en S3
        String key = "assets/img/" + productId + ext;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                //.acl("public-read")
                .contentType(file.getContentType())
                .build();

        s3.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String mime = file.getContentType();
        if (mime == null || !ALLOWED_MIME.contains(mime)) {
            throw new IllegalArgumentException("Tipo MIME no permitido");
        }

        String ext = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("Extensión no permitida");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo es demasiado grande");
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Archivo sin extensión");
        }

        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

}