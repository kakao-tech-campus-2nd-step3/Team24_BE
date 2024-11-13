package challenging.application.domain.auth.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public String deleteUser(Member member){
        memberRepository.delete(member);

        if(refreshTokenRepository.existsByMemberId(member.getId())){
            refreshTokenRepository.deleteByMemberId(member.getId());
        }

        return member.getUuid();
    }

}
