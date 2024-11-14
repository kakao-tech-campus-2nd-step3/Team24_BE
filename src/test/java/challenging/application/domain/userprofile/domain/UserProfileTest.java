package challenging.application.domain.userprofile.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.userprofile.PointNotEnoughException;
import challenging.application.global.error.userprofile.PointNotNegetiveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    @DisplayName("유저 닉네임 수정 테스트")
    void 유저_닉네임_수정_테스트(){
        //given
        String updateNickName = "수정 테스트";
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //when
        userProfile.updateUserNickName(updateNickName);

        //then
        assertThat(userProfile.getUserNickName()).isEqualTo(updateNickName);
    }

    @Test
    @DisplayName("유저 닉네임 수정 시 null 값이 들어오면 수정 안함")
    void 유저_닉네임_수정_null_들어올_때_수정_안함(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //when
        userProfile.updateUserNickName(null);

        //then
        assertThat(userProfile.getUserNickName()).isEqualTo("test");
    }

    @Test
    @DisplayName("이미지 url 수정 테스트")
    void 이미지_url_수정_테스트(){
        //given
        String imageUrl = "s3://";
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //when
        userProfile.updateImgUrl(imageUrl);

        //then
        assertThat(userProfile.getImgUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("이미지 url 수정 시 null 값이 들어오면 수정 안함")
    void 이미지_url_수정_null_들어올_때_수정_안함(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //when
        userProfile.updateImgUrl(null);

        //then
        assertThat(userProfile.getImgUrl()).isEqualTo(null);
    }

    @Test
    @DisplayName("포인트 적립할 때 음수 시 에러 발생")
    void 포인트_적립_포인트_음수_에러_발생(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //expected
        assertThatThrownBy(() -> userProfile.addPoint(-1000))
                .isInstanceOf(PointNotNegetiveException.class)
                .hasMessage(ErrorCode.POINT_NOT_NEGETIVE_ERROR.getMessage());
    }
    @Test
    @DisplayName("포인트 적립 테스트")
    void 포인트_적립_테스트(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //when
        userProfile.addPoint(1000);

        //then
        assertThat(userProfile.getPoint()).isEqualTo(3000);
    }

    @Test
    @DisplayName("포인트 사용 시 사용 포인트 음수면 에러 발생 테스트")
    void 포인트_사용_시_사용_포인트_음수면_에러_발생_테스트(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //expected
        assertThatThrownBy(() -> userProfile.usePoint(-1000))
                .isInstanceOf(PointNotNegetiveException.class)
                .hasMessage(ErrorCode.POINT_NOT_NEGETIVE_ERROR.getMessage());
    }

    @Test
    @DisplayName("포인트 사용 시 사용 포인트 보유 포인트보다 크면 에러 발생 테스트")
    void 포인트_사용_시_사용_포인트_보유_포인트보다_크면_에러_발생_테스트(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //expected
        assertThatThrownBy(() -> userProfile.usePoint(4000))
                .isInstanceOf(PointNotEnoughException.class)
                .hasMessage(ErrorCode.POINT_NOT_ENOUGH_ERROR.getMessage());
    }

    @Test
    @DisplayName("포인트 사용 테스트")
    void 포인트_사용_테스트(){
        //given
        UserProfile userProfile = new UserProfile(null, "test", 2000);

        //when
        userProfile.usePoint(1000);

        //then
        assertThat(userProfile.getPoint()).isEqualTo(1000);
    }

}