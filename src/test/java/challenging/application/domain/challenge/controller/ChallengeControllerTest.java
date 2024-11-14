//package challenging.application.challenge;
//
//import challenging.application.domain.auth.entity.Member;
//import challenging.application.domain.auth.repository.MemberRepository;
//import challenging.application.domain.category.Category;
//import challenging.application.domain.challenge.controller.ChallengeController;
//import challenging.application.domain.challenge.entity.Challenge;
//import challenging.application.domain.challenge.service.ChallengeService;
//import challenging.application.domain.userprofile.domain.UserProfile;
//import challenging.application.global.dto.request.ChallengeRequest;
//import challenging.application.global.security.utils.jwt.JWTUtils;
//import challenging.application.history.mockUser.WithMockCustomUser;
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ChallengeController.class)
//class ChallengeControllerTest {
//
//    @Autowired
//    MockMvc mvc;
//
//    @MockBean
//    JWTUtils jwtUtils;
//
//    @MockBean
//    ChallengeService challengeService;
//
//    @Mock
//    Member member;
//
//    @MockBean
//    private MemberRepository memberRepository;
//    Challenge challenge1;
//    Challenge challenge2;
//
//    @Test
//    @DisplayName("/api/challenges/{challengeId} 테스트")
//    @WithMockCustomUser
//    void GET_CHALLENGE_BY_ID_TEST() throws Exception {
//        // given
//        Long challengeId = 1L;
//
//        // when
//        given(challengeService.getChallengeById(challengeId)).willReturn(challengeResponse1);
//
//        mvc.perform(get("/api/challenges/{challengeId}", challengeId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.challengeName").value("운동 하자"))
//                .andExpect(jsonPath("$.data.point").value(1000))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("/api/challenges 전체 조회 테스트")
//    @WithMockCustomUser
//    void GET_ALL_CHALLENGES_TEST() throws Exception {
//        given(challengeService.getChallenges()).willReturn(challengeResponseList);
//
//        mvc.perform(get("/api/challenges"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data.size()").value(2))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("/api/challenges 생성 테스트")
//    @WithMockCustomUser
//    void CREATE_CHALLENGE_TEST() throws Exception {
//        ChallengeRequest challengeRequest = new ChallengeRequest(
//                1L,
//                1,
//                "운동 챌린지",
//                "운동을 하자",
//                1000,
//                "2024-10-10",
//                "10:00",
//                "12:00",
//                "abc.png",
//                2,
//                4
//        );
//        MockMultipartFile dtoFile = new MockMultipartFile("dto", "", "application/json", "{ \"name\": \"New Challenge\", \"point\": 1000 }".getBytes());
//        MockMultipartFile file = new MockMultipartFile("upload", "image.png", "image/png", "image data".getBytes());
//
//        ChallengeCreateResponse createResponse = new ChallengeCreateResponse(1L, "image.jpg");
//
//        given(challengeService.createChallenge(any(ChallengeRequest.class), any())).willReturn(createResponse);
//
//        mvc.perform(multipart("/api/challenges")
//                        .file(dtoFile)
//                        .file(file))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data.challengeId").value(1L))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("/api/challenges/{challengeId} 삭제 테스트")
//    @WithMockCustomUser
//    void DELETE_CHALLENGE_TEST() throws Exception {
//        Long challengeId = 1L;
//        ChallengeDeleteResponse deleteResponse = new ChallengeDeleteResponse(1L);
//
//        given(challengeService.deleteChallenge(challengeId, member)).willReturn(deleteResponse);
//
//        mvc.perform(delete("/api/challenges/{challengeId}", challengeId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//    }
//    @Test
//    @DisplayName("/api/challenges/reservation/{challengeId} 테스트")
//    @WithMockCustomUser
//    void RESERVE_CHALLENGE_TEST() throws Exception {
//        Long challengeId = 1L;
//        Long userId = 1L;
//        ChallengeReservationResponse reservationResponse = new ChallengeReservationResponse(challengeId, userId);
//
//        given(challengeService.reserveChallenge(challengeId, member)).willReturn(reservationResponse);
//
//        mvc.perform(post("/api/challenges/reservation/{challengeId}", challengeId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
//                .andExpect(jsonPath("$.data.userId").value(userId))
//                .andDo(print());
//    }
//
//
//    @BeforeEach
//    public void setup() throws Exception {
//        UserProfile userProfile = new UserProfile();
//
//        member = new Member("pnu@pusan.ac.kr", "pnu", "ROLE_USER",userProfile);
//
//        Field member1IdField = Member.class.getDeclaredField("id");
//        member1IdField.setAccessible(true);
//        member1IdField.set(member, 1L);
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
//                .minParticipantNum(2)
//                .maxParticipantNum(4)
//                .build();
//
//        Field idField2 = Challenge.class.getDeclaredField("id");
//        idField2.setAccessible(true);
//        idField2.set(challenge2, 2L);
//
//        challengeResponse1 = ChallengeResponse.fromEntity(challenge1, 1);
//        challengeResponse2 = ChallengeResponse.fromEntity(challenge2, 1);
//
//        challengeResponseList = List.of(challengeResponse1, challengeResponse2);
//    }
//
//}
//
