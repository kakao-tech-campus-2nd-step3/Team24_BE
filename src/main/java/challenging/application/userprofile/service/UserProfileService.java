package challenging.application.userprofile.service;

import challenging.application.dto.request.UserProfileRequest.UserProfilePutRequest;
import challenging.application.dto.response.UserProfileResponse.UserProfileGetResponse;
import challenging.application.dto.response.UserProfileResponse.UserProfilePutResponse;
import challenging.application.images.S3PresignedImageService;
import challenging.application.userprofile.domain.UserProfile;
import challenging.application.userprofile.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final S3PresignedImageService s3PresignedImageService;

    public UserProfileService(UserProfileRepository userProfileRepository,
        S3PresignedImageService s3PresignedImageService) {
        this.userProfileRepository = userProfileRepository;
        this.s3PresignedImageService = s3PresignedImageService;
    }

    public UserProfileGetResponse getUserProfile(Long memberId) {
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId).orElseThrow(
            () -> new RuntimeException()
        );
        String presignedGetUrl = null;
        if (userProfile.getImageExtension() != null && userProfile.getImageExtension() != null) {
            presignedGetUrl = s3PresignedImageService.createUserPresignedGetUrl(
                userProfile.getImageExtension(),
                userProfile.getMember().getUuid()
            );
        }

        UserProfileGetResponse userProfileGetResponse = UserProfileGetResponse.of(userProfile,presignedGetUrl);


        return userProfileGetResponse;
    }

    public UserProfilePutResponse putUserProfile(Long memberId, UserProfilePutRequest userProfilePutRequest){
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId).orElseThrow(
            () -> new RuntimeException()
        );
        userProfile.updateUserNickName(userProfilePutRequest.userNickName());
        userProfile.updateImageExtension(userProfilePutRequest.Extension());

        String presignedPutUrl = null;

        if (userProfile.getImageExtension() != null && userProfile.getImageExtension() != null) {
            presignedPutUrl = s3PresignedImageService.createUserPresignedPutUrl(
                userProfile.getImageExtension(),
                userProfile.getMember().getUuid()
            );
        }



        return new UserProfilePutResponse(userProfilePutRequest.userNickName(),presignedPutUrl);
    }


}
