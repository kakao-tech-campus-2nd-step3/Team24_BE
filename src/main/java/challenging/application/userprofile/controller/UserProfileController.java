package challenging.application.userprofile.controller;

import challenging.application.auth.annotation.LoginMember;
import challenging.application.dto.request.UserProfileRequest;
import challenging.application.dto.response.UserProfileResponse.UserProfileGetResponse;
import challenging.application.dto.response.UserProfileResponse.UserProfilePutResponse;
import challenging.application.userprofile.domain.UserProfile;
import challenging.application.auth.domain.Member; // Member 엔터티
import challenging.application.userprofile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<?> getUserProfile(@LoginMember Member user) {

        UserProfileGetResponse userProfileResponse = userProfileService.getUserProfile(user.getId());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userProfileResponse);

    }

    @PostMapping
    public ResponseEntity<?> updateUserProfile(
        @LoginMember Member user,
        @RequestBody UserProfileRequest.UserProfilePutRequest userProfilePutRequest)
    {
        UserProfilePutResponse userProfileResponse = userProfileService.putUserProfile(user.getId(),userProfilePutRequest);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userProfileResponse);

    }
}
