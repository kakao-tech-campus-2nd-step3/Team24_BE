package challenging.application.global.error.images;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ImageFileDeleteException extends Team24Exception {
  public ImageFileDeleteException() {
    super(ErrorCode.IMAGE_FILE_DELETE_ERROR);
  }

}
