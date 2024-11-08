package challenging.application.global.dto.response.userprofile;

import challenging.application.domain.userprofile.domain.UserProfile;

public record UserProfilePutResponse(
        String userNickName,
        String imgUrl
){
    public static UserProfilePutResponse of(UserProfile userProfile, String imgUrl) {
        return new UserProfilePutResponse(userProfile.getUserNickName(),imgUrl);
    }
}
