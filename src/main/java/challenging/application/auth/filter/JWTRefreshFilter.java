package challenging.application.auth.filter;

import challenging.application.auth.utils.servletUtils.jwtUtils.FilterResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static challenging.application.auth.utils.AuthConstant.*;
import static challenging.application.auth.utils.servletUtils.cookie.CookieUtils.*;


@RequiredArgsConstructor
@Slf4j
public class JWTRefreshFilter extends OncePerRequestFilter {

    private final FilterResponseUtils filterResponseUtils;

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

        if (!filterResponseUtils.isTokenInDB(response, refresh)) {
            return;
        }

        if (filterResponseUtils.isTokenExpired(response, refresh)) {
            return;
        }

        if (!filterResponseUtils.checkTokenType(response, refresh, REFRESH_TOKEN)) {
            return;
        }

        request.setAttribute(REFRESH_TOKEN,refresh);

        filterChain.doFilter(request, response);
    }

    private boolean isUrlRefresh(String requestUri) {
        return requestUri.matches("^\\/reissue(?:\\/.*)?$");
    }

}
