package challenging.application.auth.filter;

import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.servletUtils.jwtUtils.JWTResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static challenging.application.auth.servletUtils.cookie.CookieUtils.checkRefreshTokenInCookie;


@RequiredArgsConstructor
@Slf4j
public class JWTRefreshFilter extends OncePerRequestFilter {

    private final JWTResponseUtils jwtResponseUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!isUrlRefresh(request.getRequestURI())) {

            filterChain.doFilter(request, response);
            return;
        }

        String refresh = checkRefreshTokenInCookie(request);

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!jwtResponseUtils.isTokenInDB(response, refresh)) {
            return;
        }

        if (jwtResponseUtils.isTokenExpired(response, refresh)) {
            return;
        }

        if (!jwtResponseUtils.checkTokenType(response, refresh, "refresh")) {
            return;
        }

        request.setAttribute("refresh",refresh);

        filterChain.doFilter(request, response);
    }

    private boolean isUrlRefresh(String requestUri) {
        return requestUri.matches("^\\/reissue(?:\\/.*)?$");
    }

}
