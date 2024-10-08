package challenging.application.images;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
// 예시 컨트롤러임 쓰지마
@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/image/upload")
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
}
