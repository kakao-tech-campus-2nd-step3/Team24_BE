package challenging.application.domain.history.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.history.entity.History;
import challenging.application.domain.history.service.HistoryService;
import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.history.HistoryGetResponse;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.mockUser.WithMockCustomUser;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HistoryApiController.class)
class HistoryApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    JWTUtils jwtUtils;

    @MockBean
    HistoryService historyService;

    @MockBean
    MemberRepository memberRepository;
    Member member;
    Challenge challenge1;
    Challenge challenge2;
    History history1;
    History history2;

    @Test
    @DisplayName("/api/histories/{historyId} 응답 테스트")
    @WithMockCustomUser
    void api_histories_historyId_응답_테스트() throws Exception {
        //given
        String token = "AccessToken";
        String uuid = "uuid";
        ChallengeGetResponse challengeGetResponse = ChallengeGetResponse.fromEntity(challenge1, 2);
        HistoryGetResponse historyGetResponse = HistoryGetResponse.of(challengeGetResponse, history1);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(historyService.findOneHistory(member.getId(), history1.getId())).willReturn(historyGetResponse);

        //expected
        mvc.perform(get("/api/histories/{historyId}", history1.getId()))
                .andExpect(jsonPath("$.data.challenge.challengeName").value(challenge1.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/histories 전체 조회 응답 테스트")
    @WithMockCustomUser
    void api_histories_응답_테스트() throws Exception {
        //given
        String token = "AccessToken";
        String uuid = "uuid";
        ChallengeGetResponse challengeGetResponse1 = ChallengeGetResponse.fromEntity(challenge1, 2);
        HistoryGetResponse historyGetResponse1 = HistoryGetResponse.of(challengeGetResponse1, history1);

        ChallengeGetResponse challengeGetResponse2 = ChallengeGetResponse.fromEntity(challenge2, 2);
        HistoryGetResponse historyGetResponse2 = HistoryGetResponse.of(challengeGetResponse2, history2);

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(historyService.findAllHistory(member.getId())).willReturn(List.of(historyGetResponse1, historyGetResponse2));

        //expected
        mvc.perform(get("/api/histories"))
                .andExpect(jsonPath("$.data.size()").value(2))
                .andDo(print());
    }

    @BeforeEach
    void setUp() throws Exception{
        UserProfile userProfile = new UserProfile();

        member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER",userProfile);
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

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

        Field idField1 = Challenge.class.getDeclaredField("id");
        idField1.setAccessible(true);
        idField1.set(challenge1, 1L);

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

        Field idField3 = History.class.getDeclaredField("id");
        idField3.setAccessible(true);
        idField3.set(history1, 1L);

        history2 = History.builder()
                .challenge(challenge2)
                .member(member)
                .isSucceed(Boolean.FALSE)
                .isHost(Boolean.TRUE)
                .build();
    }

}