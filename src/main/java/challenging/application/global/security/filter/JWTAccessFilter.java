package challenging.application.global.security.filter;

import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.global.security.utils.servletUtils.jwtUtils.FilterResponseUtils;
import challenging.application.domain.auth.constant.AuthConstant;
import challenging.application.global.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Slf4j
public class JWTAccessFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final FilterResponseUtils filterResponseUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isUrlLogin(request.getRequestURI())) {

            filterChain.doFilter(request, response);
            return;
        }

        if (isUrlOAuth2(request.getRequestURI())) {

            filterChain.doFilter(request, response);
            return;
        }

        if (isReissue(request.getRequestURI())) {

            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(AuthConstant.AUTHORIZATION);

        //Authorization 헤더 검증
        if (checkHeader(authorization)) {

            log.info("Access Token Not Exist");
            filterResponseUtils.generateUnAuthorizationErrorResponse(ErrorCode.UNAUTHORIZED_USER_ERROR, response);
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        if (filterResponseUtils.isTokenExpired(response, token)) {
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        if (!filterResponseUtils.checkTokenType(response, token, AuthConstant.ACCESS_TOKEN)) {
            return;
        }

        //토큰에서 username과 role 획득
        Collection<GrantedAuthority> collection = getGrantedAuthorities(token);

        Authentication authToken = new UsernamePasswordAuthenticationToken(token, null, collection);

        log.info("in the jwt Filter authentication = {}", authToken);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(String token) {
        String role = jwtUtils.getRole(token);

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new SimpleGrantedAuthority(role));
        return collection;
    }

    private boolean checkHeader(String authorization) {
        return authorization == null || !authorization.startsWith(AuthConstant.BEARER);
    }

    private boolean isUrlLogin(String requestUri) {
        return requestUri.matches("^\\/login(?:\\/.*)?$");
    }

    private boolean isUrlOAuth2(String requestUri) {
        return requestUri.matches("^\\/oauth2(?:\\/.*)?$");
    }

    private boolean isReissue(String requestUri) {
        return requestUri.matches("^\\/api/reissue(?:\\/.*)?$");
    }


}
