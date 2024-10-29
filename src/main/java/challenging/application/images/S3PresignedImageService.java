package challenging.application.images;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class S3PresignedImageService {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3PresignedImageService(
        @Value("${cloud.aws.credentials.access-key}") String accessKey,
        @Value("${cloud.aws.credentials.secret-key}") String secretKey,
        @Value("${cloud.aws.region.static}") String region
    ) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();
    }

    private static final int DURATION_URL_TIME = 10;

    public String createPresignedPutUrl(String imageExtension, String uuid) {
        String keyName = "images/" + uuid.replace("-", "") + "." + imageExtension;
        String contentType = "image/" + imageExtension;

        Map<String, String> metadata = Map.of(
            "fileType", contentType,
            "Content-Type", contentType
        );

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .metadata(metadata)
            .build();

        try {
            // Create a presigned URL for a PUT request
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(DURATION_URL_TIME)) // URL expires in 10 minutes.
                .putObjectRequest(objectRequest)
                .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            String uploadUrl = presignedRequest.url().toString();

            log.info("Upload URL: " + uploadUrl);

            return uploadUrl;

        } catch (Exception e) {
            log.error("An error occurred while generating presigned URL: {}", e.getMessage());
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

    public String createPresignedGetUrl(String imageExtension, String uuid) {
        String keyName = "userprofile/" + uuid.replace("-", "") + "." + imageExtension;

        try {
            // Create a presigned URL for a GET request
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(keyName)
                .build();

            GetObjectPresignRequest getPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(DURATION_URL_TIME)) // URL expires in 10 minutes.
                .getObjectRequest(getObjectRequest)
                .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getPresignRequest);
            String downloadUrl = presignedGetObjectRequest.url().toString();

            log.info("Download URL: " + downloadUrl);

            return downloadUrl;

        } catch (Exception e) {
            log.error("An error occurred while generating presigned URL: {}", e.getMessage());
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

    private void validateImageExtension(String extension) {
        if (!extension.matches("^(jpg|jpeg|png|bmp)$")) {
            throw new IllegalArgumentException("Invalid image extension");
        }
    }
}
