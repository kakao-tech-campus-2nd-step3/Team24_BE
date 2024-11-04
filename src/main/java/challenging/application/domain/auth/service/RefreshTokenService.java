package challenging.application.domain.auth.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.entity.RefreshToken;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RefreshToken renewalRefreshToken(String previousToken, String newToken, Long expiredMs) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(previousToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 REFRESH_TOKEN 입니다."));

        Date newExpireTime = new Date(System.currentTimeMillis() + expiredMs);

        refreshToken.updateToken(newToken, newExpireTime.toString());

        return refreshToken;
    }

    @Transactional
    public RefreshToken addRefreshEntity(String refresh, String uuid, Long expiredMs) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(uuid + "의 회원이 없습니다."));

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken(member, refresh, date.toString());

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public Optional<RefreshToken> findRefreshToken(Long memberId) {
        return refreshTokenRepository.findRefreshTokenByMemberId(memberId);
    }

}
