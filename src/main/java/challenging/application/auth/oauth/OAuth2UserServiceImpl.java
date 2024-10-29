package challenging.application.auth.oauth;

import challenging.application.auth.domain.Member;
import challenging.application.auth.oauth.oauthResponse.KakaoResponse;
import challenging.application.auth.oauth.oauthResponse.NaverResponse;
import challenging.application.auth.oauth.oauthResponse.OAuth2Response;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.userprofile.domain.UserProfile;
import challenging.application.userprofile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static challenging.application.auth.utils.AuthConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("oauth user = {}",oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if(registrationId.equals(NAVER)){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals(KAKAO)){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else{
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Optional<Member> memberOptional = memberRepository.findByUsername(username);

        if(memberOptional.isPresent()){
            Member member = memberOptional.get();

            return new OAuth2UserImpl(member);
        }

        UserProfile userProfile = new UserProfile();

        Member member = new Member(username, username, oAuth2Response.getEmail(), "ROLE_USER",userProfile);

        memberRepository.save(member);

        userProfileRepository.save(userProfile);

        return new OAuth2UserImpl(member);
    }
}
