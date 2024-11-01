package challenging.application.auth.controller;

import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.service.RefreshTokenService;
import challenging.application.auth.utils.servletUtils.cookie.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static challenging.application.auth.utils.AuthConstant.*;

@Controller
@ResponseBody
@AllArgsConstructor
public class ReissueController {

    private final JWTUtils jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = (String) request.getAttribute(REFRESH_TOKEN);

        Map<String, String> jwtInformation = jwtUtil.getJWTInformation(refresh);

        String uuid = jwtInformation.get(UUID);
        String role = jwtInformation.get(ROLE);

        String newAccess = jwtUtil.generateAccessToken(uuid, role);
        String newRefresh = jwtUtil.generateRefreshToken(uuid, role);

        refreshTokenService.renewalRefreshToken(refresh, newRefresh, jwtUtil.getRefreshExpiredTime());

        response.setHeader(AUTHORIZATION, BEARER + newAccess);

        response.addCookie(CookieUtils.createCookie(REFRESH_TOKEN, newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}