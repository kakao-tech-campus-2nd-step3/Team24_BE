package challenging.application.dto.request;


public record UserProfileRequest() {

    public record UserProfilePutRequest(String userNickName,
                                        String Extension
    ){};

}
