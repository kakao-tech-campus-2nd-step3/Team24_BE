package challenging.application.auth.service;

import challenging.application.auth.domain.Member;
import challenging.application.auth.domain.RefreshToken;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.auth.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RefreshToken renewalRefreshToken(String previousToken, String newToken, String email, Long expiredMs){
        refreshTokenRepository.deleteByToken(previousToken);

        return addRefreshEntity(newToken, email, expiredMs);
    }

    @Transactional
    public RefreshToken addRefreshEntity(String refresh, String email, Long expiredMs) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(email + "의 회원이 없습니다."));

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken(member, refresh, date.toString());

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }
}
