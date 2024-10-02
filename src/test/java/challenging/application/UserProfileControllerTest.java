package challenging.application;



import challenging.application.auth.jwt.JWTUtils;
import challenging.application.controller.UserProfileController;
import challenging.application.dto.UserProfileResponseDTO;
import challenging.application.model.UserProfile;
import challenging.application.service.UserProfileService;
import challenging.application.auth.domain.Member;
import challenging.application.auth.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserProfileController userProfileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("성공적으로 사용자 프로필을 조회할 수 있다")
    void testGetUserProfileSuccess() {
        // given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("token");
        when(jwtUtils.getEmail("token")).thenReturn("test@example.com");

        Member member = new Member("testuser", "testnickname", "test@example.com", "ROLE_USER");
        member.setId(1L);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1L);
        userProfile.setUserNickName("testnickname");
        userProfile.setImageUrl("testimage");
        userProfile.setPoint(100);

        when(userProfileService.getUserProfileByMemberId(1L)).thenReturn(Optional.of(userProfile));
        UserProfileResponseDTO responseDTO = new UserProfileResponseDTO("testnickname", "testimage", 100);
        when(userProfileService.convertToDTO(userProfile)).thenReturn(responseDTO);

        // when
        ResponseEntity<?> response = userProfileController.getUserProfile();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    @DisplayName("인증되지 않은 사용자 프로필 조회 시 UNAUTHORIZED 반환")
    void testGetUserProfileUnauthenticated() {
        // given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        // when
        ResponseEntity<?> response = userProfileController.getUserProfile();

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not authenticated", response.getBody());
    }

    @Test
    @DisplayName("사용자 프로필 업데이트 성공")
    void testUpdateUserProfileSuccess() {
        // given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("token");
        when(jwtUtils.getEmail("token")).thenReturn("test@example.com");

        Member member = new Member("testuser", "testnickname", "test@example.com", "ROLE_USER");
        member.setId(1L);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1L);
        userProfile.setProfileId(1L); // 프로필 ID 설정

        when(userProfileService.getUserProfileByMemberId(1L)).thenReturn(Optional.of(userProfile));

        Map<String, String> updates = Map.of("user_nick_name", "newNickname");
        when(userProfileService.updateUserProfileFields(userProfile, updates)).thenReturn(true);

        // when
        ResponseEntity<?> response = userProfileController.updateUserProfile(updates);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Map.of("user_id", userProfile.getProfileId()), response.getBody()); // profileId가 null이 아님
    }
}
