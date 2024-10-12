package challenging.application.images;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class ImageService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region; // 지역 정보를 별도로 유지

    // 로컬 경로 설정
    private String localLocation = "/Users/pakjeongwoo/Downloads/";

    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String imageload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");

        if (file == null) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);

        // S3에 이미지 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(uuidFileName)
            .acl(ObjectCannedACL.PUBLIC_READ) // 퍼블릭 읽기 권한 부여
            .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(localFile));

        // S3 URL 생성
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, uuidFileName);

        // 로컬 파일 삭제
        if (!localFile.delete()) {
            System.err.println("로컬 파일 삭제 실패: " + localPath);
        }

        return s3Url;
    }
}
