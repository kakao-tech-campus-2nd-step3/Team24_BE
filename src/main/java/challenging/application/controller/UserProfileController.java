package challenging.application.controller;

import challenging.application.auth.jwt.JWTUtils;
import challenging.application.dto.UserProfileResponseDTO;
import challenging.application.model.UserProfile;
import challenging.application.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JWTUtils jwtUtils;

    public UserProfileController(UserProfileService userProfileService, JWTUtils jwtUtils) {
        this.userProfileService = userProfileService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getPrincipal();
            String email = jwtUtils.getEmail(token);
            Optional<UserProfile> optionalUserProfile = userProfileService.getUserProfileByEmail(email);

            UserProfile userProfile;
            if (optionalUserProfile.isPresent()) {
                userProfile = optionalUserProfile.get();
            } else {
                // 프로필 생성
                userProfile = userProfileService.createNewUserProfile(email);
            }

            UserProfileResponseDTO responseDTO = userProfileService.convertToDTO(userProfile);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }

    @PostMapping
    public ResponseEntity<?> updateUserProfile(@RequestBody Map<String, String> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getPrincipal();
            String email = jwtUtils.getEmail(token);
            Optional<UserProfile> optionalUserProfile = userProfileService.getUserProfileByEmail(email);

            if (optionalUserProfile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            UserProfile userProfile = optionalUserProfile.get();

            // 회원정보 업데이트 처리
            boolean isUpdated = userProfileService.updateUserProfileFields(userProfile, updates);

            if (!isUpdated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one field (user_nick_name, user_body, image_url) must be provided");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("user_id", userProfile.getProfileId()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }
}
