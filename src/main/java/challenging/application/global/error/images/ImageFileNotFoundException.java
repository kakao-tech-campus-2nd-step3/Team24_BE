package challenging.application.global.error.images;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ImageFileNotFoundException extends Team24Exception {
  public ImageFileNotFoundException() {
    super(ErrorCode.IMAGE_FILE_NOT_FOUND_ERROR);
  }

}
