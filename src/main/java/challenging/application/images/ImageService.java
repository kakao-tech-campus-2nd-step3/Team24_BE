//package challenging.application.images;
//
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartRequest;
//
//@Service
//public class ImageService {
//    private final S3Config s3Config;
//
//    public ImageService(S3Config s3Config) {
//        this.s3Config = s3Config;
//    }
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//    //  로컬 경로 넣어줌
//    private String localLocation = "/Users/pakjeongwoo/Downloads";
//
//    public String imageload(MultipartRequest request) throws IOException {
//
//        MultipartFile file = request.getFile("upload");
//
//        String fileName = file.getOriginalFilename();
//        String ext = fileName.substring(fileName.indexOf("."));
//
//        String uuidFileName = UUID.randomUUID() + ext;
//        String localPath = localLocation + uuidFileName;
//
//        File localFile = new File(localPath);
//        file.transferTo(localFile);
//
//        // s3에 이미지 올림
//        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket,uuidFileName,localFile).withCannedAcl(
//            CannedAccessControlList.PublicRead));
//
//        String s3Url = s3Config.amazonS3Client().getUrl(bucket,uuidFileName).toString();
//        // 저장필요
//
//        //서버 저장 이미지 삭제
//        localFile.delete();
//
//        return s3Url;
//
//    }
//}
