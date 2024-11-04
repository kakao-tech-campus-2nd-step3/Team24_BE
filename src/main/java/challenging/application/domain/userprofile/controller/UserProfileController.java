package challenging.application.domain.userprofile.controller;

import challenging.application.domain.auth.security.annotation.LoginMember;
import challenging.application.global.dto.request.UserProfileRequest;
import challenging.application.global.dto.response.UserProfileResponse.UserProfileGetResponse;
import challenging.application.global.dto.response.UserProfileResponse.UserProfilePutResponse;
import challenging.application.domain.auth.entity.Member; // Member 엔터티
import challenging.application.domain.userprofile.service.UserProfileService;
import challenging.application.global.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUserProfile(@LoginMember Member user) {

        UserProfileGetResponse userProfileResponse = userProfileService.getUserProfile(user.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponse(userProfileResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> updateUserProfile(
            @LoginMember Member user,
            @RequestBody UserProfileRequest.UserProfilePutRequest userProfilePutRequest) {
        UserProfilePutResponse userProfileResponse = userProfileService.putUserProfile(user.getId(),
                userProfilePutRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponse(userProfileResponse));
    }
}
