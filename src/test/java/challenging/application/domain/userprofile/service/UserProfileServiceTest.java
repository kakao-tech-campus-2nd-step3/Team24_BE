package challenging.application.domain.userprofile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.domain.userprofile.repository.UserProfileRepository;
import challenging.application.global.dto.response.userprofile.HostProfileGetResponse;
import challenging.application.global.dto.response.userprofile.UserProfileGetResponse;
import challenging.application.global.dto.response.userprofile.UserProfilePutResponse;
import challenging.application.global.images.ImageService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {
    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    @DisplayName("유저 id를 활용하여 유저 프로필 조회 테스트")
    void 유저_아이디를_활용한_유저_프로필_조회() {
        // Given
        Long memberId = 1L;
        UserProfile mockUserProfile = new UserProfile(null, "test", 2000);

        given(userProfileRepository.findByMemberId(memberId)).willReturn(Optional.of(mockUserProfile));

        // When
        UserProfileGetResponse userProfileGetResponse = userProfileService.getUserProfile(memberId);

        // Then
        assertThat(userProfileGetResponse.userNickName()).isEqualTo("test");
        assertThat(userProfileGetResponse.point()).isEqualTo(2000);
    }

    @Test
    @DisplayName("유저 id를 활용하여 유저 프로필 수정 테스트")
    void 유저_아이디를_활용한_유저_프로필_수정() {
        // Given
        Long memberId = 1L;
        String putNickName = "수정 이름";
        MultipartFile image = new MockMultipartFile("test",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test".getBytes());
        String newS3Url = "s3://new-image-url";

        UserProfile mockUserProfile = new UserProfile(null, "test", 2000);

        given(userProfileRepository.findByMemberId(memberId)).willReturn(Optional.of(mockUserProfile));
        given(imageService.imageloadUserProfile(image, memberId)).willReturn(newS3Url);

        // When
        UserProfilePutResponse userProfilePutResponse = userProfileService.putUserProfile(memberId, putNickName, image);

        // Then
        assertThat(userProfilePutResponse.userNickName()).isEqualTo(putNickName);
        assertThat(userProfilePutResponse.imgUrl()).isEqualTo(newS3Url);
    }

    @Test
    @DisplayName("호스트 profile 조회 기능 테스트")
    void 호스트_프로필_조회_기능_테스트(){
        //given
        String uuid = "uuid";
        UserProfile mockUserProfile = new UserProfile(null, "test", 2000);
        given(userProfileRepository.findByMemberUuid(uuid)).willReturn(Optional.of(mockUserProfile));

        //when
        HostProfileGetResponse hostProfile = userProfileService.getHostProfile(uuid);

        //then
        assertThat(hostProfile.userNickName()).isEqualTo(mockUserProfile.getUserNickName());
    }
}
