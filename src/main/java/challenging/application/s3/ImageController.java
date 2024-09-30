package challenging.application.s3;

import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

@RestController
public class ImageController {

    @PostMapping("/image/upload")
    @ResponseBody
    public Map<String, Object> imageUpload(MultipartRequest multipartRequest){



        return 
    }
}
