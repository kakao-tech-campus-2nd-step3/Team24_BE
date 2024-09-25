package challenging.application.controller;


import challenging.application.auth.jwt.JWTUtils;
import challenging.application.model.UserProfile;
import challenging.application.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/userprofile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private JWTUtils jwtUtils;

    // 기존의 GET 메서드
    @GetMapping
    public UserProfile getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getPrincipal();
            String email = jwtUtils.getEmail(token);
            UserProfile userProfile = userProfileService.getUserProfileByEmail(email);

            if (userProfile == null) {
                userProfile = new UserProfile();
                userProfile.setUserId(email);
                userProfile.setUserNickName("New User");
                userProfile.setUserBody("Default body");
                userProfile.setImageUrl("Default image URL");
                userProfile.setPoint(1000);
                userProfileService.saveUserProfile(userProfile);
            }
            return userProfile;
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }

    // 새로운 POST 메서드: 사용자 정보 수정
    @PostMapping
    public ResponseEntity<?> updateUserProfile(@RequestBody Map<String, String> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getPrincipal();
            String email = jwtUtils.getEmail(token);
            UserProfile userProfile = userProfileService.getUserProfileByEmail(email);

            if (userProfile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // 업데이트할 필드가 있는지 확인
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

            if (!isUpdated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one field (user_nick_name, user_body, image_url) must be provided");
            }

            // 수정된 프로필 저장
            userProfileService.saveUserProfile(userProfile);

            // user_id 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("user_id", userProfile.getProfileId()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }
}
