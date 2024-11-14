package challenging.application.global.error.images;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ImageFileUploadException extends Team24Exception {
  public ImageFileUploadException() {
    super(ErrorCode.IMAGE_FILE_UPLOAD_ERROR);
  }

}
