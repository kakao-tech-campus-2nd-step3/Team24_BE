package challenging.application.images;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class S3Uploader {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Uploader(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    private static final int DURATION_URL_TIME = 10;

    public String createPresignedPutUrl(String imageExtension, String uuid) {

        String keyName = "images/" + uuid + "." + imageExtension;
        keyName = keyName.replace("-", ""); // uuid는 보통 -값들어가서 삭제

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
            // presigned URL 생성 (PUT 요청)
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(DURATION_URL_TIME))  // URL expires in 10 minutes.
                .putObjectRequest(objectRequest)
                .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            String uploadUrl = presignedRequest.url().toString();

            log.info("Presigned URL to upload a file to: {}", uploadUrl);

            return uploadUrl;

        } catch (AmazonS3Exception e) {
            // S3 서비스에서 발생하는 오류를 처리
            log.error("S3 관련 오류 발생. 상태 코드: {}, 오류 메시지: {}", e.getStatusCode(), e.getErrorMessage());
            throw new RuntimeException("S3에서 presigned URL을 생성하는 중 오류가 발생했습니다.", e);
        } catch (AmazonServiceException e) {
            // AWS 서비스 전체에서 발생하는 오류를 처리
            log.error("AWS 서비스에서 오류 발생. 상태 코드: {}, AWS 오류 코드: {}, 요청 ID: {}",
                e.getStatusCode(), e.getErrorCode(), e.getRequestId());
            throw new RuntimeException("AWS 서비스에서 presigned URL을 생성하는 중 오류가 발생했습니다.", e);
        } catch (SdkClientException e) {
            // SDK 클라이언트 측에서 발생하는 오류를 처리 (예: 네트워크 문제)
            log.error("클라이언트 측 오류 발생: {}", e.getMessage());
            throw new RuntimeException("AWS SDK 클라이언트에서 presigned URL을 생성하는 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            // 기타 모든 예외 처리
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Presigned URL 생성 중 알 수 없는 오류가 발생했습니다.", e);
        }
    }



    private void validateImageExtension(String extension) {
        if (!extension.matches("^(jpg|jpeg|png|gif|bmp)$")) {
            throw new IllegalArgumentException("Invalid image extension");
        }
    }
}
