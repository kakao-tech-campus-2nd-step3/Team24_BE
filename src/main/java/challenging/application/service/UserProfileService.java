package challenging.application.service;

import challenging.application.model.UserProfile;
import challenging.application.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    // 이메일을 기반으로 UserProfile을 조회하는 메소드
    public UserProfile getUserProfileByEmail(String email) {
        return userProfileRepository.findByUserId(email).orElse(null);
    }

    // 새로운 UserProfile 저장 메소드
    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
}
