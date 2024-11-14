package challenging.application.domain.auth.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.entity.RefreshToken;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.repository.RefreshTokenRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("레프레시 토큰을 업데이트하여 리뉴얼한다.")
     void 리프레시_토큰_업데이트_리뉴얼(){
        //given
        String previousToken = "previous";
        String newToken = "new";
        RefreshToken refreshToken = new RefreshToken(null, previousToken, null);

        given(refreshTokenRepository.findByToken(previousToken)).willReturn(Optional.of(refreshToken));

        //when
        RefreshToken updateRefreshToken = refreshTokenService.renewalRefreshToken(previousToken, newToken, 1000L);

        //then
        assertThat(updateRefreshToken.getToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("리프레시 토큰을 DB에 저장한다.")
    void 리프레시_토큰_디비_저장_테스트() throws Exception{
        //given
        String newToken = "new";
        String uuid = "uuid";
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", null);

        Field idField = Member.class.getDeclaredField("uuid");
        idField.setAccessible(true);
        idField.set(member, uuid);

        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));

        //when
        RefreshToken savedRefreshToken = refreshTokenService.addRefreshEntity(newToken, uuid, 1000L);

        //then
        assertThat(savedRefreshToken.getToken()).isEqualTo(newToken);
        assertThat(savedRefreshToken.getMember().getUuid()).isEqualTo(uuid);
        verify(refreshTokenRepository, times(1)).save(savedRefreshToken);
    }
}