package challenging.application.global.error.images;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class S3Exception extends Team24Exception {
  public S3Exception() {
    super(ErrorCode.S3_NETWORK_ERROR);
  }

}
