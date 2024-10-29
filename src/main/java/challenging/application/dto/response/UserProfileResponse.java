package challenging.application.dto.response;


import challenging.application.history.domain.History;
import challenging.application.userprofile.domain.UserProfile;

public record UserProfileResponse() {
    public record UserProfileGetResponse(String userNickName,
                                         Integer point,
                                         String imgUrl
                                         ){
        public static UserProfileGetResponse of(UserProfile userProfile,String imgUrl) {
            return new UserProfileGetResponse(userProfile.getUserNickName(),userProfile.getPoint(),imgUrl);
        }
    };

    public record UserProfilePutResponse(String userNickName,
                                         String imgUrl){
        public static UserProfilePutResponse of(UserProfile userProfile,String imgUrl) {
            return new UserProfilePutResponse(userProfile.getUserNickName(),imgUrl);
        }
    }


}
