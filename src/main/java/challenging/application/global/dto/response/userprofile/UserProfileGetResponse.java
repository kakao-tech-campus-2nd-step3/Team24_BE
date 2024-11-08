package challenging.application.global.dto.response.userprofile;

import challenging.application.domain.userprofile.domain.UserProfile;

public record UserProfileGetResponse(String userNickName,
                                     Integer point,
                                     String imgUrl
){
    public static UserProfileGetResponse of(UserProfile userProfile, String imgUrl) {
        return new UserProfileGetResponse(userProfile.getUserNickName(),userProfile.getPoint(),imgUrl);
    }
};