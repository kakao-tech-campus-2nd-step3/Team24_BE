package challenging.application.global.security.utils.servletUtils.jwtUtils;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.response.ErrorResult;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.domain.auth.repository.RefreshTokenRepository;
import challenging.application.global.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class FilterResponseUtils {

    private final JWTUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    public boolean isTokenExpired(HttpServletResponse response, String token) {
        try {
            jwtUtils.isExpired(token);
        } catch (ExpiredJwtException e) {
            generateTokenErrorResponse(ErrorCode.TOKEN_EXPIRED_ERROR, response);
            return true;
        } catch (MalformedJwtException e) {
            generateTokenErrorResponse(ErrorCode.TOKEN_MALFORMED_ERROR, response);
            return true;
        } catch (UnsupportedJwtException e) {
            generateTokenErrorResponse(ErrorCode.TOKEN_UNSUPPORTED_ERROR, response);
            return true;
        } catch (SignatureException e) {
            generateTokenErrorResponse(ErrorCode.TOKEN_SIGNATURE_ERROR, response);
            return true;
        } catch (Exception e) {
            generateTokenErrorResponse(ErrorCode.TOKEN_ERROR, response);
            return true;
        }
        return false;
    }

    public boolean checkTokenType(HttpServletResponse response, String token, String type) {
        String category = jwtUtils.getCategory(token);

        if (!category.equals(type)) {

            generateTokenErrorResponse(ErrorCode.TOKEN_ERROR, response);
            return false;
        }
        return true;
    }

    public void generateTokenErrorResponse(ErrorCode errorCode, HttpServletResponse response) {
        ErrorResult errorResult = new ErrorResult(errorCode.getStatus().value(), errorCode.getMessage());

        setErrorResponseInFilter(response, HttpServletResponse.SC_BAD_REQUEST, errorResult);
    }

    public void generateUnAuthorizationErrorResponse(ErrorCode errorCode, HttpServletResponse response) {
        ErrorResult errorResult = new ErrorResult(errorCode.getStatus().value(), errorCode.getMessage());

        setErrorResponseInFilter(response, HttpServletResponse.SC_UNAUTHORIZED, errorResult);
    }

    public boolean isTokenInDB(HttpServletResponse response, String token) {
        Boolean isExist = refreshTokenRepository.existsByToken(token);
        if (!isExist) {

            generateTokenErrorResponse(ErrorCode.TOKEN_ERROR, response);
            return false;
        }
        return true;
    }

    private void setErrorResponseInFilter(HttpServletResponse response, int responseStatus, ErrorResult errorResult) {
        String errorResponse;

        try {
            errorResponse = objectMapper.writeValueAsString(ApiResponse.errorResponse(errorResult));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errorResponse);
            response.setStatus(responseStatus);
        }
        catch (IOException e){
            log.error("IOException 발생");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }
}
