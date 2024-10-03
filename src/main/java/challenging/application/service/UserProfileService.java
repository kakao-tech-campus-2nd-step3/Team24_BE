package challenging.application.service;

import challenging.application.dto.UserProfileResponseDTO;
import challenging.application.model.UserProfile;
import java.util.Map;
import java.util.Optional;

public interface UserProfileService {

    // Member의 id로 프로필을 조회
    Optional<UserProfile> getUserProfileByMemberId(Long memberId);

    // 프로필 저장
    UserProfile saveUserProfile(UserProfile userProfile);

    // UserProfile을 DTO로 변환
    UserProfileResponseDTO convertToDTO(UserProfile userProfile);

    // 새로운 프로필 생성 시 Member의 id를 사용
    UserProfile createNewUserProfile(Long memberId);

    // 회원정보 업데이트
    boolean updateUserProfileFields(UserProfile userProfile, Map<String, String> updates);
}
