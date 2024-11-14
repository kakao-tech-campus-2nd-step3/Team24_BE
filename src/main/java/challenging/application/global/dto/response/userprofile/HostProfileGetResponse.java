package challenging.application.global.dto.response.userprofile;

import challenging.application.domain.userprofile.domain.UserProfile;

public record HostProfileGetResponse(String userNickName,
                                     String imgUrl
) {
    public static HostProfileGetResponse of(UserProfile userProfile) {
        return new HostProfileGetResponse(userProfile.getUserNickName(),userProfile.getImgUrl());
    }


}
