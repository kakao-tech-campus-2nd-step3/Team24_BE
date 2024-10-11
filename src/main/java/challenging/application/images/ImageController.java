package challenging.application.images;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
// 예시 컨트롤러임 쓰지마
@RestController
public class ImageController {

    private final ImageService imageService;
    private final S3PresignedImageService s3PresignedImageService;

    public ImageController(ImageService imageService,
        S3PresignedImageService s3PresignedImageService) {
        this.imageService = imageService;
        this.s3PresignedImageService = s3PresignedImageService;
    }

    @PostMapping("/api/image/upload")
    @ResponseBody
    public Map<String, Object> imageUpload(MultipartRequest multipartRequest)  {

        Map<String, Object> responseData = new HashMap<>();

        try{
            String s3Url = imageService.imageload(multipartRequest);


            responseData.put("uploaded",true);
            responseData.put("url",s3Url);
        } catch (IOException e){
            responseData.put("uploaded",false);
        }
        return responseData;
    }

    @GetMapping("/api/image")
    public String imageUrlGet(@RequestParam String extenion,@RequestParam String uuid){
        String presignedGetUrl = s3PresignedImageService.createPresignedGetUrl(extenion, uuid);
        return presignedGetUrl;
    }

    @PutMapping("/api/image")
    public String imageUrlPut(@RequestParam String extenion, @RequestParam String uuid){
        String presignedPutUrl = s3PresignedImageService.createPresignedPutUrl(extenion, uuid);
        return presignedPutUrl;
    }
}

