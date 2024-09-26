package challenging.application.service;

import challenging.application.dto.UserProfileResponseDTO;
import challenging.application.model.UserProfile;
import java.util.Map;
import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfile> getUserProfileByEmail(String email);

    UserProfile saveUserProfile(UserProfile userProfile);

    UserProfileResponseDTO convertToDTO(UserProfile userProfile);

    UserProfile createNewUserProfile(String email);

    boolean updateUserProfileFields(UserProfile userProfile, Map<String, String> updates);
}
