package challenging.application.auth.filter;

import challenging.application.auth.repository.RefreshTokenRepository;
import challenging.application.auth.utils.servletUtils.jwtUtils.FilterResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static challenging.application.auth.utils.AuthConstant.*;
import static challenging.application.auth.utils.servletUtils.cookie.CookieUtils.*;

@AllArgsConstructor
public class JWTLogoutFilter extends GenericFilterBean {

    private final RefreshTokenRepository refreshRepository;
    private final FilterResponseUtils filterResponseUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!isUrlLogout(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!isHttpMethodPost(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = checkRefreshTokenInCookie(request);

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!filterResponseUtils.isTokenInDB(response, refresh)) {
            return;
        }

        if (filterResponseUtils.isTokenExpired(response, refresh)) {
            return;
        }

        if (!filterResponseUtils.checkTokenType(response, refresh, REFRESH_TOKEN)) {
            return;
        }

        refreshRepository.deleteByToken(refresh);

        clearCookie(response);
    }

    private boolean isHttpMethodPost(String requestMethod) {
        return requestMethod.equals("POST");
    }

    private boolean isUrlLogout(String requestUri) {
        return requestUri.matches("^\\/logout$");
    }

}