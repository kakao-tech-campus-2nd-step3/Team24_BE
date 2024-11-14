package challenging.application.domain.userprofile.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.history.service.HistoryService;
import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.domain.userprofile.service.UserProfileService;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.history.HistoryGetResponse;
import challenging.application.global.dto.response.userprofile.UserProfileGetResponse;
import challenging.application.global.dto.response.userprofile.UserProfilePutResponse;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.mockUser.WithMockCustomUser;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserProfileController.class)
class UserProfileControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    JWTUtils jwtUtils;

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    MemberRepository memberRepository;

    Member member;
    UserProfile userProfile;

    @BeforeEach
    void set_up() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";
        userProfile = new UserProfile(null, "testNickName", 2000);
        member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", userProfile);
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
    }

    @Test
    @DisplayName("/api/userprofile 유저 프로필 조회 테스트")
    @WithMockCustomUser
    void api_userprofile_응답_테스트() throws Exception {
        //given
        given(userProfileService.getUserProfile(member.getId())).willReturn(UserProfileGetResponse.of(userProfile));

        //expected
        mvc.perform(get("/api/userprofile"))
                .andExpect(jsonPath("$.data.userNickName").value(userProfile.getUserNickName()))
                .andExpect(jsonPath("$.data.point").value(userProfile.getPoint()))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/userprofile post 유저 프로필 수정 테스트")
    @WithMockCustomUser
    void api_userprofile_post_응답_테스트() throws Exception {
        //given
        String userNickName = "testNickName";
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test image content".getBytes());

        given(userProfileService.putUserProfile(member.getId(), userNickName, image))
                .willReturn(UserProfilePutResponse.of(userProfile, "s3://"));

        //expected
        mvc.perform(multipart(HttpMethod.POST, "/api/userprofile")
                        .file(image)
                        .part(new MockPart("userNickName", userNickName.getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(jsonPath("$.data.userNickName").value(userNickName))
                .andExpect(jsonPath("$.data.imgUrl").value("s3://"))
                .andDo(print());
    }
}