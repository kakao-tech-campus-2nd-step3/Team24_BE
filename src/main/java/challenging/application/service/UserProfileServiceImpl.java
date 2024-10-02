package challenging.application.service;

import challenging.application.dto.UserProfileResponseDTO;
import challenging.application.model.UserProfile;
import challenging.application.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    // Member의 id로 프로필을 조회하는 메서드로 수정
    @Override
    public Optional<UserProfile> getUserProfileByMemberId(Long memberId) {
        return userProfileRepository.findByUserId(memberId);
    }

    // 프로필 저장
    @Override
    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    // UserProfile을 DTO로 변환
    @Override
    public UserProfileResponseDTO convertToDTO(UserProfile userProfile) {
        return new UserProfileResponseDTO(
            userProfile.getUserNickName(),
            userProfile.getImageUrl(),
            userProfile.getPoint()
        );
    }

    // 새로운 프로필 생성 시 Member의 id를 사용
    @Override
    public UserProfile createNewUserProfile(Long memberId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(memberId);  // Member의 id 값을 설정
        userProfile.setUserNickName("New User");
        userProfile.setImageUrl("Default image URL");
        userProfile.setPoint(1000);
        return saveUserProfile(userProfile);
    }

    // 회원정보 업데이트 로직
    @Override
    public boolean updateUserProfileFields(UserProfile userProfile, Map<String, String> updates) {
        boolean isUpdated = false;

        if (updates.containsKey("user_nick_name")) {
            userProfile.setUserNickName(updates.get("user_nick_name"));
            isUpdated = true;
        }

        if (updates.containsKey("image_url")) {
            userProfile.setImageUrl(updates.get("image_url"));
            isUpdated = true;
        }

        if (isUpdated) {
            saveUserProfile(userProfile);
        }

        return isUpdated;
    }
}
