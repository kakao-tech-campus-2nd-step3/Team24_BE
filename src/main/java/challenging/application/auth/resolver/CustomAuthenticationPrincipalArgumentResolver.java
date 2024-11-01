package challenging.application.auth.resolver;

import challenging.application.auth.annotation.LoginMember;
import challenging.application.auth.domain.Member;
import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtils jwtUtils;

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAuthenticationMember(authentication)) {
            log.info("Authentication 객체가 없거나, 익명 사용자 입니다.");
            return null;
        }

        Member member = getMemberFromAuthentication(authentication);

        log.info("member name = {}", member.getUsername());

        return member;
    }

    private Member getMemberFromAuthentication(Authentication authentication) {
        // jwt token 추출
        String token = (String) authentication.getPrincipal();

        String uuid = jwtUtils.getUUID(token);

        return memberRepository.findByEmail(uuid).orElseThrow();
    }

    private boolean isAuthenticationMember(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
