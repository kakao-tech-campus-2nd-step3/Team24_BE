package challenging.application.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.service.AuthService;
import challenging.application.domain.auth.service.RefreshTokenService;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.mockUser.WithMockCustomUser;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthApiController.class)
class AuthApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    JWTUtils jwtUtils;

    @MockBean
    AuthService authService;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    MemberRepository memberRepository;

    @Test
    @DisplayName("/api/auth 응답 테스트")
    @WithMockCustomUser
    void api_auth_응답_테스트() throws Exception {
        //given
        String token = "AccessToken";
        String uuid = "uuid";
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", null);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));

        mvc.perform(get("/api/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("인증된 회원입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/logout 응답 테스트")
    @WithMockCustomUser
    void api_logout_응답_테스트() throws Exception {
        //given
        String token = "AccessToken";
        String uuid = "uuid";
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", null);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));

        mvc.perform(post("/api/logout").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그아웃이 성공적으로 처리되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/auth delete : 회원탈퇴 응답 테스트")
    @WithMockCustomUser
    void api_auth_delete_회원탈퇴_응답_테스트() throws Exception {
        //given
        String token = "AccessToken";
        String uuid = "uuid";
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", null);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(authService.deleteUser(member)).willReturn(uuid);

        mvc.perform(delete("/api/auth").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 탈퇴가 완료되었습니다."))
                .andExpect(jsonPath("$.data.uuid").value(uuid))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/reissue 토큰 재발급 테스트")
    @WithMockCustomUser
    void api_auth_reissue_토큰_재발급_응답_테스트() throws Exception {
        //given
        String token = "AccessToken";
        String newAccessToken = "AccessToken";
        String newRefreshToken = "RefreshToken";
        String uuid = "uuid";
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", null);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(jwtUtils.generateAccessToken(any(), any())).willReturn(newAccessToken);
        given(jwtUtils.generateRefreshToken(any(), any())).willReturn(newRefreshToken);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));

        mvc.perform(post("/api/reissue").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("토큰이 정상적으로 재발급 되었습니다."))
                .andExpect(jsonPath("$.data.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(newRefreshToken))
                .andDo(print());
    }
}