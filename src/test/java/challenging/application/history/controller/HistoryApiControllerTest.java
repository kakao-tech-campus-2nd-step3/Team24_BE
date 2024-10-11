package challenging.application.history.controller;

import challenging.application.challenge.domain.Category;
import challenging.application.challenge.domain.Challenge;
import challenging.application.history.domain.History;
import challenging.application.history.mockUser.WithMockCustomUser;
import challenging.application.auth.domain.Member;
import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.dto.response.HistoryResponse;
import challenging.application.history.service.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoryApiController.class)
class HistoryApiControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    HistoryServiceImpl historyService;
    @MockBean
    JWTUtils jwtUtils;
    @MockBean
    MemberRepository memberRepository;

    Member member;
    Challenge challenge1;
    Challenge challenge2;
    History history1;
    History history2;
    ChallengeResponse challengeResponse1;
    ChallengeResponse challengeResponse2;
    HistoryResponse historyResponse1;
    HistoryResponse historyResponse2;

    @BeforeEach
    public void setup() throws Exception {
        dataSetUp();
    }

    @Test
    @DisplayName("/api/histories/{historyId} 테스트")
    @WithMockCustomUser
    void API_HISTORIES_ID_TEST() throws Exception {
        //given
        String token = "AccessToken";
        String email = "pnu@pusan.ac.kr";
        Long historyId = 1L;

        given(jwtUtils.getEmail(token)).willReturn(email);
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        //when
        given(historyService.findOneHistory(member.getId(), historyId)).willReturn(historyResponse1);

        //expected
        mvc.perform(get("/api/histories/{historyId}",historyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challenge.challengeName").value(challengeResponse1.challengeName()))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/histories 테스트")
    @WithMockCustomUser
    void API_HISTORIES_TEST() throws Exception {
        //given
        String token = "AccessToken";
        String email = "pnu@pusan.ac.kr";

        given(jwtUtils.getEmail(token)).willReturn(email);
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        //when
        List<HistoryResponse> historyResponses = Arrays.asList(historyResponse1, historyResponse2);

        given(historyService.findAllHistory(member.getId())).willReturn(historyResponses);

        //expected
        mvc.perform(get("/api/histories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(historyResponses.size()))
                .andDo(print());
    }

    private void dataSetUp() throws Exception{
        member = new Member("pnu", "pnu", "pnu@pusan.ac.kr","ROLE_USER");

        challenge1 = Challenge.builder()
                .body("운동 챌린지")
                .category(Category.SPORT)
                .date(LocalDate.now())
                .point(1000)
                .name("운동 하자")
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .host(member)
                .imageUrl("abc.png")
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
                .imageUrl("abc2.png")
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
                .imageUrl("abc.png")
                .build();

        history2 = History.builder()
                .challenge(challenge2)
                .member(member)
                .isSucceed(Boolean.FALSE)
                .isHost(Boolean.TRUE)
                .imageUrl("abc.png")
                .build();

        challengeResponse1 = ChallengeResponse.fromEntity(challenge1, 1);
        historyResponse1 = HistoryResponse.of(challengeResponse1, history1);

        challengeResponse2 = ChallengeResponse.fromEntity(challenge2, 1);
        historyResponse2 = HistoryResponse.of(challengeResponse2, history2);
    }

}