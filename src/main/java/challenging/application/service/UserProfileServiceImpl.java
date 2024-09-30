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

    @Override
    public Optional<UserProfile> getUserProfileByEmail(String email) {
        return userProfileRepository.findByUserId(email);
    }

    @Override
    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfileResponseDTO convertToDTO(UserProfile userProfile) {
        return new UserProfileResponseDTO(
            userProfile.getUserNickName(),
            userProfile.getUserBody(),
            userProfile.getImageUrl(),
            userProfile.getPoint()
        );
    }

    // 새로운 프로필 생성
    @Override
    public UserProfile createNewUserProfile(String email) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(email);
        userProfile.setUserNickName("New User");
        userProfile.setUserBody("Default body");
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

        if (updates.containsKey("user_body")) {
            userProfile.setUserBody(updates.get("user_body"));
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
