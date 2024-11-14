package challenging.application.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.repository.RefreshTokenRepository;
import challenging.application.domain.userprofile.domain.UserProfile;
import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void 회원_탈퇴_테스트(){
        //given
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", new UserProfile());

        //when
        String deleteUser = authService.deleteUser(member);

        //then
        assertThat(deleteUser).isEqualTo(member.getUuid());
        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    @DisplayName("회원 탈퇴 시 리프레시 토큰 함께 삭제 테스트")
    void 회원_탈퇴_시_레프레시_토큰_함께_삭제_테스트() throws Exception {
        //given
        Member member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER", new UserProfile());
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

        given(refreshTokenRepository.existsByMemberId(member.getId())).willReturn(Boolean.TRUE);

        //when
        String deleteUser = authService.deleteUser(member);

        //then
        assertThat(deleteUser).isEqualTo(member.getUuid());
        verify(memberRepository, times(1)).delete(member);
        verify(refreshTokenRepository, times(1)).deleteByMemberId(member.getId());
    }
}