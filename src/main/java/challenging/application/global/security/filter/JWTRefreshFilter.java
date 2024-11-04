package challenging.application.global.security.filter;

import challenging.application.domain.auth.repository.RefreshTokenRepository;
import challenging.application.global.security.utils.servletUtils.jwtUtils.FilterResponseUtils;
import challenging.application.domain.auth.constant.AuthConstant;
import challenging.application.global.security.utils.servletUtils.cookie.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JWTRefreshFilter extends OncePerRequestFilter {

    private final RefreshTokenRepository refreshRepository;
    private final FilterResponseUtils filterResponseUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!isUrlRefresh(request.getRequestURI())) {

            filterChain.doFilter(request, response);
            return;
        }

        String refresh = CookieUtils.checkRefreshTokenInCookie(request);

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!filterResponseUtils.isTokenInDB(response, refresh)) {
            return;
        }

        if (filterResponseUtils.isTokenExpired(response, refresh)) {
            refreshRepository.deleteByToken(refresh);
            return;
        }

        if (!filterResponseUtils.checkTokenType(response, refresh, AuthConstant.REFRESH_TOKEN)) {
            return;
        }

        request.setAttribute(AuthConstant.REFRESH_TOKEN, refresh);

        filterChain.doFilter(request, response);
    }

    private boolean isUrlRefresh(String requestUri) {
        return requestUri.matches("^\\/reissue(?:\\/.*)?$");
    }

}
