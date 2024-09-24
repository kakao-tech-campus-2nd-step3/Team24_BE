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

        String email = jwtInformation.get(EMAIL);
        String role = jwtInformation.get(ROLE);

        //make new JWT
        String newAccess = jwtUtil.generateAccessToken(email, role);
        String newRefresh = jwtUtil.generateRefreshToken(email, role);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenService.renewalRefreshToken(refresh, newRefresh, email, jwtUtil.getRefreshExpiredTime());

        //response
        response.setHeader(AUTHORIZATION, BEARER + newAccess);

        response.addCookie(CookieUtils.createCookie(REFRESH_TOKEN, newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}