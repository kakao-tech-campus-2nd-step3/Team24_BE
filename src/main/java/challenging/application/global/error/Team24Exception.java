package challenging.application.global.error;

public class Team24Exception extends RuntimeException{
    private final ErrorCode errorCode;

    public Team24Exception(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public int getStatusCode(){
        return errorCode.getStatus().value();
    }
}
