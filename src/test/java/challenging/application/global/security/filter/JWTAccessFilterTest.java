package challenging.application.global.security.filter;

import challenging.application.domain.auth.controller.AuthController;
import challenging.application.domain.auth.service.RefreshTokenService;
import challenging.application.global.error.ErrorCode;
import challenging.application.global.security.configuration.SecurityConfiguration;
import challenging.application.global.security.configuration.WebConfig;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.global.security.oauth.OAuth2SuccessHandler;
import challenging.application.global.security.oauth.OAuth2UserServiceImpl;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.repository.RefreshTokenRepository;
import challenging.application.global.security.utils.servletUtils.jwtUtils.FilterResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfiguration.class, WebConfig.class})
public class JWTAccessFilterTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    OAuth2UserServiceImpl oAuth2UserService;

    @MockBean
    OAuth2SuccessHandler oAuth2SuccessHandler;

    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    JWTUtils jwtUtils;

    @SpyBean
    FilterResponseUtils responseUtils;

    @MockBean
    WebSecurityCustomizer webSecurityCustomizer;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    RefreshTokenService refreshTokenService;

    String token;
    String tokenRefresh;

    @BeforeEach
    void setUp(){
        token = jwtUtils.generateAccessToken("test", "USER_ROLE");
        tokenRefresh = jwtUtils.generateRefreshToken("test", "USER_ROLE");
    }


    @Test
    @DisplayName("정상 통과")
    void 정상_통과() throws Exception {
        mvc.perform(get("/api/auth")
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Authorization 컨트롤러 입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("Header 에 Token 없을 시 401 Error")
    void HEADER_TOKEN_없을때_401_TEST() throws Exception {
        mvc.perform(get("/api/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value(ErrorCode.UNAUTHORIZED_USER_ERROR.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("Header 에 Token 값이 올바르지 않을 때 400 Error")
    void HEADER_TOKEN_값_이상_400_TEST() throws Exception {
        token = token + "test 용 이상한 값";

        mvc.perform(get("/api/auth")
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(ErrorCode.TOKEN_SIGNATURE_ERROR.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("Header 에 Token 값이 만료 되었을 때 400 Error")
    void HEADER_TOKEN_만료_400_TEST(@Value("${spring.jwt.secret}") String secret) throws Exception {
        //given
        JWTUtils testJwt = new JWTUtils(secret);

        Field idField = JWTUtils.class.getDeclaredField("accessExpiredTime");
        idField.setAccessible(true);
        idField.set(testJwt, 1L);

        token = testJwt.generateAccessToken("test", "ROLE_USER");

        //expected
        mvc.perform(get("/api/auth")
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(ErrorCode.TOKEN_EXPIRED_ERROR.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("Header에 Token이 AccessToken이 아닐 시 Error")
    void HEADER_TOKEN_ACCESS_TOKEN_아닐_시_ERROR() throws Exception {
        //expected
        mvc.perform(get("/api/auth")
                        .header("Authorization","Bearer " + tokenRefresh))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(ErrorCode.TOKEN_ERROR.getMessage()))
                .andDo(print());
    }
}
