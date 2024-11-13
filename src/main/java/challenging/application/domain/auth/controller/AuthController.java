package challenging.application.domain.auth.controller;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.service.AuthService;
import challenging.application.global.dto.response.ApiResponse;
import challenging.application.global.dto.response.auth.AuthDeleteResponse;
import challenging.application.global.dto.response.auth.TokenResponse;
import challenging.application.global.security.annotation.LoginMember;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.domain.auth.service.RefreshTokenService;
import challenging.application.global.security.utils.servletUtils.cookie.CookieUtils;
import challenging.application.domain.auth.constant.AuthConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtils jwtUtil;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<?>> authorization() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponseWithMessage("Authorization 컨트롤러 입니다.",null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponseWithMessage("로그아웃이 성공적으로 처리되었습니다.", null));
    }

    @DeleteMapping("/auth")
    public ResponseEntity<ApiResponse<?>> deleteUser(@LoginMember Member member) {
        String deletedUuid = authService.deleteUser(member);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponseWithMessage("회원 탈퇴가 완료되었습니다.", AuthDeleteResponse.from(deletedUuid)));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = (String) request.getAttribute(AuthConstant.REFRESH_TOKEN);

        Map<String, String> jwtInformation = jwtUtil.getJWTInformation(refresh);

        String uuid = jwtInformation.get(AuthConstant.UUID);
        String role = jwtInformation.get(AuthConstant.ROLE);

        String newAccess = jwtUtil.generateAccessToken(uuid, role);
        String newRefresh = jwtUtil.generateRefreshToken(uuid, role);

        refreshTokenService.renewalRefreshToken(refresh, newRefresh, jwtUtil.getRefreshExpiredTime());

        response.setHeader(AuthConstant.AUTHORIZATION, AuthConstant.BEARER + newAccess);
        response.addCookie(CookieUtils.createCookie(AuthConstant.REFRESH_TOKEN, newRefresh));

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponseWithMessage("토큰이 정상적으로 재발급 되었습니다.", new TokenResponse(newAccess, newAccess)
                ));
    }
}