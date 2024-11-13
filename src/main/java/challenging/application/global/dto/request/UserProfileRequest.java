package challenging.application.global.dto.request;


public record UserProfileRequest() {
    public record UserProfilePutRequest(String userNickName
    ){};

}
