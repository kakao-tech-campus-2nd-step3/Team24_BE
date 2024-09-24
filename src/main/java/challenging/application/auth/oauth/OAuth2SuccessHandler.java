package challenging.application.auth.oauth;

import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.service.RefreshTokenService;
import challenging.application.auth.utils.servletUtils.cookie.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import static challenging.application.auth.utils.AuthConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtils jwtUtil;
    private final RefreshTokenService refreshTokenService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2UserImpl customUserDetails = (OAuth2UserImpl) authentication.getPrincipal();

        String email = customUserDetails.getEmail();

        String role = getRole(authentication);

        String accessToken = jwtUtil.generateAccessToken(email, role);

        String refreshToken = jwtUtil.generateRefreshToken(email, role);

        refreshTokenService.addRefreshEntity(refreshToken, email, jwtUtil.getRefreshExpiredTime());

        log.info("Access = {}", accessToken);
        log.info("Refresh = {}", refreshToken);

        setInformationInResponse(response, accessToken, refreshToken);
    }

    private void setInformationInResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        Cookie access = CookieUtils.createCookie(ACCESS_TOKEN, accessToken);
        Cookie refresh = CookieUtils.createCookie(REFRESH_TOKEN, refreshToken);

        response.addCookie(access);
        response.addCookie(refresh);

        response.setStatus(HttpStatus.OK.value());

        response.sendRedirect("http://localhost:8080/");
    }

    private String getRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        return auth.getAuthority();
    }
}
