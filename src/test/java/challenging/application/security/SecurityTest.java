package challenging.application.security;

import challenging.application.auth.configuration.SecurityConfiguration;
import challenging.application.auth.configuration.WebConfig;
import challenging.application.auth.controller.AuthController;
import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.oauth.OAuth2SuccessHandler;
import challenging.application.auth.oauth.OAuth2UserServiceImpl;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.auth.repository.RefreshTokenRepository;
import challenging.application.auth.utils.servletUtils.jwtUtils.FilterResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.test.web.servlet.MockMvc;

import static challenging.application.exception.ExceptionMessage.SIGNATURE_EXCEPTION;
import static challenging.application.exception.ExceptionMessage.UNAUTHORIZED_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfiguration.class, WebConfig.class})
public class SecurityTest {

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

    String token;

    @BeforeEach
    void setUp(){
        token = jwtUtils.generateAccessToken("test", "USER_ROLE");
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
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED_USER))
                .andDo(print());
    }

    @Test
    @DisplayName("Header 에 Token 값이 올바르지 않을 때 401 Error")
    void HEADER_TOKEN_값_이상_401_TEST() throws Exception {
        token = token + "test 용 이상한 값";

        mvc.perform(get("/api/auth")
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(SIGNATURE_EXCEPTION))
                .andDo(print());
    }

}
