package challenging.application;


import challenging.application.userprofile.domain.UserProfile;
import challenging.application.userprofile.repository.UserProfileRepository;
import challenging.application.userprofile.service.UserProfileService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Member의 id로 프로필 조회 성공")
    void testGetUserProfileByMemberIdSuccess() {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1L);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(userProfile));

        // when
        Optional<UserProfile> result = userProfileService.getUserProfileByMemberId(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(userProfile, result.get());
    }

    @Test
    @DisplayName("프로필 생성 성공")
    void testCreateNewUserProfileSuccess() {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1L);
        userProfile.setUserNickName("New User");  // 기대하는 값 설정
        userProfile.setImageUrl("Default image URL");  // 기대하는 값 설정
        userProfile.setPoint(1000);  // 기대하는 기본 포인트 설정

        // Mock save 메서드에서 기대한 값 반환
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // when
        UserProfile result = userProfileService.createNewUserProfile(1L);

        // then
        assertEquals(userProfile.getUserId(), result.getUserId());  // userId 확인
        assertEquals("New User", result.getUserNickName());  // userNickName 확인
        assertEquals("Default image URL", result.getImageUrl());  // imageUrl 확인
        assertEquals(1000, result.getPoint());  // point 확인
    }


    @Test
    @DisplayName("프로필 업데이트 성공")
    void testUpdateUserProfileFieldsSuccess() {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1L);

        Map<String, String> updates = Map.of("user_nick_name", "newNickname");
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);

        // when
        boolean result = userProfileService.updateUserProfileFields(userProfile, updates);

        // then
        assertTrue(result);
        assertEquals("newNickname", userProfile.getUserNickName());
    }
}
