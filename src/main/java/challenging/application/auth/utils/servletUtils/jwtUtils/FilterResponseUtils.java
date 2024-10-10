package challenging.application.auth.utils.servletUtils.jwtUtils;

import challenging.application.exception.ErrorResult;
import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static challenging.application.exception.ExceptionMessage.*;

@Component
@RequiredArgsConstructor
public class FilterResponseUtils {

    private final JWTUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    public boolean isTokenExpired(HttpServletResponse response, String token) throws IOException {
        try {
            jwtUtils.isExpired(token);
        } catch (ExpiredJwtException e) {
            generateUnauthorizedErrorResponse(EXPIRED_JWT_EXCEPTION, response);
            return true;
        } catch (MalformedJwtException e){
            generateUnauthorizedErrorResponse(MALFORMED_JWT_EXCEPTION, response);
            return true;
        } catch (UnsupportedJwtException e){
            generateUnauthorizedErrorResponse(UNSUPPORTED_JWT_EXCEPTION, response);
            return true;
        } catch (SignatureException e){
            generateUnauthorizedErrorResponse(SIGNATURE_EXCEPTION, response);
            return true;
        } catch (Exception e){
            generateUnauthorizedErrorResponse(JWT_EXCEPTION, response);
            return true;
        }
        return false;
    }

    public boolean checkTokenType(HttpServletResponse response, String token, String type) {
        String category = jwtUtils.getCategory(token);

        if (!category.equals(type)) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        return true;
    }

    public void generateUnauthorizedErrorResponse(String message, HttpServletResponse response) throws IOException {
        ErrorResult errorResult = new ErrorResult("401", message);
        String jsonResponse = objectMapper.writeValueAsString(errorResult);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public boolean isTokenInDB(HttpServletResponse response, String token) {
        Boolean isExist = refreshTokenRepository.existsByToken(token);
        if (!isExist) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        return true;
    }

}
