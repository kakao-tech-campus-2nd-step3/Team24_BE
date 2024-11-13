package challenging.application.domain.userprofile.service;

import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.domain.userprofile.repository.UserProfileRepository;
import challenging.application.global.dto.response.userprofile.UserProfileGetResponse;
import challenging.application.global.dto.response.userprofile.UserProfilePutResponse;
import challenging.application.global.images.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ImageService imageService;

    public UserProfileService(UserProfileRepository userProfileRepository,
        ImageService imageService) {
        this.userProfileRepository = userProfileRepository;
        this.imageService = imageService;
    }

    public UserProfileGetResponse getUserProfile(Long memberId) {
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId).orElseThrow(
            () -> new RuntimeException()
        );

        UserProfileGetResponse userProfileGetResponse = UserProfileGetResponse.of(userProfile);

        return userProfileGetResponse;
    }

    public UserProfilePutResponse putUserProfile(Long memberId, String userNickname, MultipartFile image){
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId).orElseThrow(
            () -> new RuntimeException()
        );

        String s3Url = userProfile.getImgUrl();

        if (image != null){
            s3Url = imageService.imageload(image, memberId);
            userProfile.updateImgUrl(s3Url);
        }

        userProfile.updateUserNickName(userNickname);

        return new UserProfilePutResponse(userNickname,s3Url);
    }


}
