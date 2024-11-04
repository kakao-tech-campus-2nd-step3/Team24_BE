package challenging.application.global.security.oauth;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.entity.RefreshToken;
import challenging.application.global.dto.response.ApiResponse;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.service.RefreshTokenService;
import challenging.application.global.security.utils.servletUtils.cookie.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static challenging.application.domain.auth.constant.AuthConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtils jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2UserImpl customUserDetails = (OAuth2UserImpl) authentication.getPrincipal();

        String uuid = customUserDetails.getUUID();

        String role = getRole(authentication);

        Optional<Member> findMember = memberRepository.findByUuid(uuid);

        Optional<RefreshToken> findRefreshToken = refreshTokenService.findRefreshToken(findMember.get().getId());

        String refreshToken = null;

        if (findRefreshToken.isEmpty()) {
            refreshToken = jwtUtil.generateRefreshToken(uuid, role);
            refreshTokenService.addRefreshEntity(refreshToken, uuid, jwtUtil.getRefreshExpiredTime());
        } else {
            refreshToken = findRefreshToken.get().getToken();
        }

        String accessToken = jwtUtil.generateAccessToken(uuid, role);

        log.info("Access = {}", accessToken);
        log.info("Refresh = {}", refreshToken);

        setInformationInResponse(response, accessToken, refreshToken);
    }

    private void setInformationInResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        Cookie access = CookieUtils.createCookie(ACCESS_TOKEN, accessToken);
        Cookie refresh = CookieUtils.createCookie(REFRESH_TOKEN, refreshToken);

        response.addCookie(access);
        response.addCookie(refresh);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ApiResponse<?> loginApiResponse = new ApiResponse<>("success", 200, "로그인 처리가 완료 되었습니다.", null);

        String loginResponse = objectMapper.writeValueAsString(loginApiResponse);

        response.getWriter().write(loginResponse);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private String getRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        return auth.getAuthority();
    }
}
