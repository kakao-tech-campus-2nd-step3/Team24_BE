package challenging.application.history.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.service.ChallengeService;
import challenging.application.domain.history.service.HistoryServiceImpl;
import challenging.application.global.dto.response.ChallengeResponse;
import challenging.application.global.dto.response.HistoryResponse;
import challenging.application.global.error.history.HistoryNotFoundException;
import challenging.application.domain.history.entity.History;
import challenging.application.domain.history.repository.HistoryRepository;
import challenging.application.domain.userprofile.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static challenging.application.global.error.ExceptionMessage.HISTORY_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {

    @InjectMocks
    HistoryServiceImpl historyService;

    @Mock
    HistoryRepository historyRepository;

    @Mock
    ChallengeService challengeService;

    Member member;
    Challenge challenge1;
    Challenge challenge2;
    History history1;
    History history2;

    @Test
    @DisplayName("user history 조회 성공")
    void HISTORY_조회_성공() {
        //given
        ChallengeResponse challengeResponse = ChallengeResponse.fromEntity(challenge1, 2);
        given(historyRepository.findHistoryByMemberIdAndId(1L, 1L)).willReturn(Optional.of(history1));
        given(challengeService.findOneChallenge(history1.getChallenge().getId())).willReturn(challengeResponse);

        //when
        HistoryResponse findHistory = historyService.findOneHistory(1L, 1L);

        //then
        assertAll(
                () -> assertThat(findHistory.challenge().challengeBody()).isEqualTo(challenge1.getBody()),
                () -> assertThat(findHistory.is_host()).isEqualTo(history1.getHost()),
                () -> assertThat(findHistory.is_succeed()).isEqualTo(history1.getSucceed())
        );
    }

    @Test
    @DisplayName("user history 조회 실패 Not Found Entity")
    void HISTORY_조회_실패_EXCEPTION(){
        //given
        given(historyRepository.findHistoryByMemberIdAndId(anyLong(), anyLong())).willReturn(Optional.empty());

        //expected
        assertThatThrownBy(() -> historyService.findOneHistory(anyLong(), anyLong()))
                .isInstanceOf(HistoryNotFoundException.class)
                .hasMessage(HISTORY_NOT_FOUND);
    }

    @Test
    @DisplayName("user history 전체 조회")
    void HISTORY_전체_조회()  {
        //given
        List<History> histories = new ArrayList<>();

        histories.add(history1);
        histories.add(history2);

        given(historyRepository.findAllByMemberId(anyLong())).willReturn(histories);
        given(challengeService.findOneChallenge(history1.getChallenge().getId())).willReturn(ChallengeResponse.fromEntity(challenge1, 2));
        given(challengeService.findOneChallenge(history2.getChallenge().getId())).willReturn(ChallengeResponse.fromEntity(challenge2, 2));
        //when

        List<HistoryResponse> historyResponses = historyService.findAllHistory(1L);

        assertThat(historyResponses.size()).isEqualTo(2);
    }

    @BeforeEach
    void setUp() throws Exception{
        UserProfile userProfile = new UserProfile();

        member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER",userProfile);

        challenge1 = Challenge.builder()
                .body("운동 챌린지")
                .category(Category.SPORT)
                .date(LocalDate.now())
                .point(1000)
                .name("운동 하자")
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .host(member)
                .imageExtension("png")
                .minParticipantNum(2)
                .maxParticipantNum(4)
                .build();

        Field idField = Challenge.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(challenge1, 1L);

        challenge2 = Challenge.builder()
                .body("운동 챌린지2")
                .category(Category.SPORT)
                .date(LocalDate.now())
                .point(2000)
                .name("운동 하자2")
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .host(member)
                .imageExtension("png")
                .minParticipantNum(2)
                .maxParticipantNum(4)
                .build();

        Field idField2 = Challenge.class.getDeclaredField("id");
        idField2.setAccessible(true);
        idField2.set(challenge2, 2L);

        history1 = History.builder()
                .challenge(challenge1)
                .member(member)
                .isSucceed(Boolean.FALSE)
                .isHost(Boolean.TRUE)
                .build();

        history2 = History.builder()
                .challenge(challenge2)
                .member(member)
                .isSucceed(Boolean.FALSE)
                .isHost(Boolean.TRUE)
                .build();
    }

}