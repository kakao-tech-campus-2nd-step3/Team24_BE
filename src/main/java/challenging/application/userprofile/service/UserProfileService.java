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
        // 이미지 작업하고 던져야함
        String presignedGetUrl = s3PresignedImageService.createPresignedGetUrl(
            userProfile.getImageExtension(),
            userProfile.getMember().getUuid()
        );
        UserProfileGetResponse userProfileGetResponse = UserProfileGetResponse.of(userProfile,presignedGetUrl);

        return userProfileGetResponse;
    }

    public UserProfilePutResponse putUserProfile(Long memberId, UserProfilePutRequest userProfilePutRequest){
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId).orElseThrow(
            () -> new RuntimeException()
        );
        userProfile.updateUserNickName(userProfilePutRequest.userNickName());
        userProfile.updateImageExtension(userProfilePutRequest.Extension());

        String presignedPutUrl = s3PresignedImageService.createPresignedPutUrl(
            userProfile.getImageExtension(),
            userProfile.getMember().getUuid()
        );


        return new UserProfilePutResponse(userProfilePutRequest.userNickName(),presignedPutUrl);
    }


}
