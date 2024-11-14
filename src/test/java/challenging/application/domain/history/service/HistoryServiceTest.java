package challenging.application.domain.history.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.service.ChallengeService;
import challenging.application.domain.history.entity.History;
import challenging.application.domain.history.repository.HistoryRepository;
import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.history.HistoryGetResponse;
import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.history.HistoryNotFoundException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {
    @InjectMocks
    HistoryService historyService;

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
        ChallengeGetResponse challengeGetResponse = ChallengeGetResponse.fromEntity(challenge1, 2);
        given(historyRepository.findHistoryByMemberIdAndId(1L, 1L)).willReturn(Optional.of(history1));
        given(challengeService.findOneChallenge(history1.getChallenge().getId())).willReturn(challengeGetResponse);

        //when
        HistoryGetResponse findHistory = historyService.findOneHistory(1L, 1L);

        //then
        assertAll(
                () -> assertThat(findHistory.challenge().challengeBody()).isEqualTo(challenge1.getBody()),
                () -> assertThat(findHistory.isHost()).isEqualTo(history1.getHost()),
                () -> assertThat(findHistory.isSucceed()).isEqualTo(history1.getSucceed())
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
                .hasMessage(ErrorCode.HISTORY_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("user history 전체 조회")
    void HISTORY_전체_조회()  {
        //given
        List<History> histories = new ArrayList<>();

        histories.add(history1);
        histories.add(history2);

        given(historyRepository.findAllByMemberId(anyLong())).willReturn(histories);
        given(challengeService.findOneChallenge(history1.getChallenge().getId()))
                .willReturn(ChallengeGetResponse.fromEntity(challenge1, 2));
        given(challengeService.findOneChallenge(history2.getChallenge().getId()))
                .willReturn(ChallengeGetResponse.fromEntity(challenge2, 2));
        //when

        List<HistoryGetResponse> historyGetRespons = historyService.findAllHistory(1L);

        assertThat(historyGetRespons.size()).isEqualTo(2);
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