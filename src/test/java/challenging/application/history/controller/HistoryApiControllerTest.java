//package challenging.application.history.controller;
//
//import challenging.application.domain.category.Category;
//import challenging.application.domain.challenge.entity.Challenge;
//import challenging.application.domain.history.controller.HistoryApiController;
//import challenging.application.domain.history.entity.History;
//import challenging.application.history.mockUser.WithMockCustomUser;
//import challenging.application.domain.auth.entity.Member;
//import challenging.application.global.security.utils.jwt.JWTUtils;
//import challenging.application.domain.auth.repository.MemberRepository;
//import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
//import challenging.application.global.dto.response.history.HistoryGetResponse;
//import challenging.application.domain.userprofile.domain.UserProfile;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HistoryApiController.class)
//class HistoryApiControllerTest {
//
//    @Autowired
//    MockMvc mvc;
//    @MockBean
//    HistoryServiceImpl historyService;
//    @MockBean
//    JWTUtils jwtUtils;
//    @MockBean
//    MemberRepository memberRepository;
//
//    Member member;
//    Challenge challenge1;
//    Challenge challenge2;
//    History history1;
//    History history2;
//    ChallengeGetResponse challengeGetResponse1;
//    ChallengeGetResponse challengeGetResponse2;
//    HistoryGetResponse historyGetResponse1;
//    HistoryGetResponse historyGetResponse2;
//
//    @BeforeEach
//    public void setup() throws Exception {
//        dataSetUp();
//    }
//
//    @Test
//    @DisplayName("/api/histories/{historyId} 테스트")
//    @WithMockCustomUser
//    void API_HISTORIES_ID_TEST() throws Exception {
//        //given
//        String token = "AccessToken";
//        String email = "pnu@pusan.ac.kr";
//        Long historyId = 1L;
//
//        given(jwtUtils.getUUID(token)).willReturn(email);
//        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
//
//        //when
//        given(historyService.findOneHistory(member.getId(), historyId)).willReturn(historyGetResponse1);
//
//        //expected
//        mvc.perform(get("/api/histories/{historyId}",historyId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.challenge.challengeName").value(challengeGetResponse1.challengeName()))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("/api/histories 테스트")
//    @WithMockCustomUser
//    void API_HISTORIES_TEST() throws Exception {
//        //given
//        String token = "AccessToken";
//        String email = "pnu@pusan.ac.kr";
//
//        given(jwtUtils.getUUID(token)).willReturn(email);
//        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
//
//        //when
//        List<HistoryGetResponse> historyGetRespons = Arrays.asList(historyGetResponse1, historyGetResponse2);
//
//        given(historyService.findAllHistory(member.getId())).willReturn(historyGetRespons);
//
//        //expected
//        mvc.perform(get("/api/histories"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(historyGetRespons.size()))
//                .andDo(print());
//    }
//
//    private void dataSetUp() throws Exception{
//        UserProfile userProfile = new UserProfile();
//
//        member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER",userProfile);
//
//        challenge1 = Challenge.builder()
//                .body("운동 챌린지")
//                .category(Category.SPORT)
//                .date(LocalDate.now())
//                .point(1000)
//                .name("운동 하자")
//                .startTime(LocalTime.now())
//                .endTime(LocalTime.now())
//                .host(member)
//                .imageExtension("png")
//                .minParticipantNum(2)
//                .maxParticipantNum(4)
//                .build();
//
//        Field idField = Challenge.class.getDeclaredField("id");
//        idField.setAccessible(true);
//        idField.set(challenge1, 1L);
//
//        challenge2 = Challenge.builder()
//                .body("운동 챌린지2")
//                .category(Category.SPORT)
//                .date(LocalDate.now())
//                .point(2000)
//                .name("운동 하자2")
//                .startTime(LocalTime.now())
//                .endTime(LocalTime.now())
//                .host(member)
//                .imageExtension("png")
//                .minParticipantNum(2)
//                .maxParticipantNum(4)
//                .build();
//
//        Field idField2 = Challenge.class.getDeclaredField("id");
//        idField2.setAccessible(true);
//        idField2.set(challenge2, 2L);
//
//        history1 = History.builder()
//                .challenge(challenge1)
//                .member(member)
//                .isSucceed(Boolean.FALSE)
//                .isHost(Boolean.TRUE)
//                .build();
//
//        history2 = History.builder()
//                .challenge(challenge2)
//                .member(member)
//                .isSucceed(Boolean.FALSE)
//                .isHost(Boolean.TRUE)
//                .build();
//
//        challengeGetResponse1 = ChallengeGetResponse.fromEntity(challenge1, 1);
//        historyGetResponse1 = HistoryGetResponse.of(challengeGetResponse1, history1);
//
//        challengeGetResponse2 = ChallengeGetResponse.fromEntity(challenge2, 1);
//        historyGetResponse2 = HistoryGetResponse.of(challengeGetResponse2, history2);
//    }
//
//}
