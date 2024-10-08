package challenging.application.history.service;

import challenging.application.auth.domain.Member;
import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.service.ChallengeService;
import challenging.application.domain.Category;
import challenging.application.exception.challenge.HistoryNotFoundException;
import challenging.application.history.domain.History;
import challenging.application.history.repository.HistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static challenging.application.exception.ExceptionMessage.HISTORY_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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



}