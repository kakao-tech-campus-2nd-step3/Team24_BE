package challenging.application.global.images;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class ImageService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket2}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region; // 지역 정보를 별도로 유지

    // 로컬 경로 설정
    private String localLocation = "/Users/pakjeongwoo/Downloads/";

    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String imageload(MultipartFile multipartFile, Long id) {
        String s3Url = null;
        try {
            if (multipartFile == null || multipartFile.isEmpty()) {
                throw new IllegalArgumentException("업로드할 파일이 없습니다.");
            }

            // 파일 이름 생성
            String originalFileName = multipartFile.getOriginalFilename();
            String ext = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
            String uniqueFileName = id + ext;

            // 로컬 경로에 파일 저장
            String localPath = localLocation + uniqueFileName;
            File localFile = new File(localPath);
            multipartFile.transferTo(localFile);

            // S3에 이미지 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uniqueFileName)
                .acl(ObjectCannedACL.PUBLIC_READ) // 퍼블릭 읽기 권한 부여
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(localFile));

            // S3 URL 생성
            s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, uniqueFileName);

            // 로컬 파일 삭제
            if (!localFile.delete()) {
                System.err.println("로컬 파일 삭제 실패: " + localPath);
            }
        } catch (IOException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            System.err.println("예기치 않은 오류 발생: " + e.getMessage());
            throw new RuntimeException("파일 처리 중 예기치 않은 오류가 발생했습니다.", e);
        }

        return s3Url;
    }


    public void deleteImageByUrl(String s3Url) {
        try {
            // URL에서 버킷과 키 추출
            String key = s3Url.substring(s3Url.lastIndexOf("/") + 1);

            // S3에서 이미지 삭제
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("이미지가 성공적으로 삭제되었습니다: " + key);
        } catch (Exception e) {
            System.err.println("이미지 삭제 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.", e);
        }
    }

}
