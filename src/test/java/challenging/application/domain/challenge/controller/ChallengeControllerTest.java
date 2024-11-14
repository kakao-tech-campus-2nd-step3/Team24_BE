package challenging.application.domain.challenge.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.service.ChallengeService;
import challenging.application.domain.history.entity.History;
import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.global.dto.request.ChallengeRequest;
import challenging.application.global.dto.request.ChallengeVoteRequest;
import challenging.application.global.dto.response.chalenge.ChallengeCancelResponse;
import challenging.application.global.dto.response.chalenge.ChallengeCreateResponse;
import challenging.application.global.dto.response.chalenge.ChallengeDeleteResponse;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.chalenge.ChallengeReservationResponse;
import challenging.application.global.dto.response.history.HistoryGetResponse;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.mockUser.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChallengeController.class)
class ChallengeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    JWTUtils jwtUtils;

    @MockBean
    ChallengeService challengeService;

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    ObjectMapper objectMapper;

    Member member;
    Challenge challenge1;
    Challenge challenge2;
    History history1;
    History history2;

    @Test
    @DisplayName("api/challenges/{challengeId} get 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_challengeId_단건_조회_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        ChallengeGetResponse challengeGetResponse = ChallengeGetResponse.fromEntity(challenge1, 2);
        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(challengeService.getChallengeById(challenge1.getId())).willReturn(challengeGetResponse);

        //expected
        mvc.perform(get("/api/challenges/{challengeId}", challenge1.getId()))
                .andExpect(jsonPath("$.data.challengeId").value(challenge1.getId()))
                .andExpect(jsonPath("$.data.challengeName").value(challenge1.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges/{challengeId} delete 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_challengeId_삭제_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        ChallengeDeleteResponse challengeDeleteResponse = new ChallengeDeleteResponse(challenge1.getId());
        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(challengeService.deleteChallenge(challenge1.getId(), member)).willReturn(challengeDeleteResponse);

        //expected
        mvc.perform(delete("/api/challenges/{challengeId}", challenge1.getId())
                        .with(csrf()))
                .andExpect(jsonPath("$.data.challengeId").value(challenge1.getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges get 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_전체_조회_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        ChallengeGetResponse challengeGetResponse1 = ChallengeGetResponse.fromEntity(challenge1, 2);
        ChallengeGetResponse challengeGetResponse2 = ChallengeGetResponse.fromEntity(challenge2, 2);
        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(challengeService.getChallengesByDate()).willReturn(List.of(challengeGetResponse1, challengeGetResponse2));

        //expected
        mvc.perform(get("/api/challenges"))
                .andExpect(jsonPath("$.data.size()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges/waiting get 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_waiting_대기_챌린지_조회_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        ChallengeGetResponse challengeGetResponse1 = ChallengeGetResponse.fromEntity(challenge1, 2);
        ChallengeGetResponse challengeGetResponse2 = ChallengeGetResponse.fromEntity(challenge2, 2);
        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(challengeService.findWaitingChallenges(member)).willReturn(List.of(challengeGetResponse1, challengeGetResponse2));

        //expected
        mvc.perform(get("/api/challenges/waiting"))
                .andExpect(jsonPath("$.data.size()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges post 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_챌린지_생성_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test image content".getBytes());
        ChallengeCreateResponse challengeCreateResponse = new ChallengeCreateResponse(3L, "s3://", "test.com");

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(challengeService.createChallenge(any(), any())).willReturn(challengeCreateResponse);

        //expected
        mvc.perform(multipart(HttpMethod.POST, "/api/challenges")
                        .file(image)
                        .param("hostUuid", "test")
                        .param("challengeName", "test")
                        .param("challengeBody", "test")
                        .param("categoryId", "1")
                        .param("point", "100")
                        .param("challengeDate", "2024-01-01")
                        .param("startTime", "10:00")
                        .param("endTime", "12:00")
                        .param("minParticipantNum", "5")
                        .param("maxParticipantNum", "20")
                        .param("challengeUrl", "test.com")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.challengeId").value(3))
                .andExpect(jsonPath("$.data.imgUrl").value("s3://"))
                .andExpect(jsonPath("$.data.challengeUrl").value("test.com"))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges/reservation/{challengeId} post 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_reservation_challengeId_예약_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        ChallengeReservationResponse challengeReservationResponse = new ChallengeReservationResponse(challenge1.getId(), member.getUuid(), challenge1.getChallengeUrl());
        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));
        given(challengeService.reserveChallenge(challenge1.getId(), member)).willReturn(challengeReservationResponse);

        //expected
        mvc.perform(post("/api/challenges/reservation/{challengeId}", challenge1.getId())
                        .with(csrf()))
                .andExpect(jsonPath("$.data.challengeId").value(challenge1.getId()))
                .andExpect(jsonPath("$.data.challengeUrl").value(challenge1.getChallengeUrl()))
                .andExpect(jsonPath("$.data.uuid").value(member.getUuid()))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges/{challengeId}/cancel post 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_challengeId_cancel_취소_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));

        //expected
        mvc.perform(post("/api/challenges/{challengeId}/cancel", challenge1.getId())
                        .with(csrf()))
                .andExpect(jsonPath("$.data.challengeId").value(challenge1.getId()))
                .andExpect(jsonPath("$.data.uuid").value(member.getUuid()))
                .andExpect(jsonPath("$.message").value("챌린지를 취소하였습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("api/challenges/{challengeId}/vote post 응답 테스트")
    @WithMockCustomUser
    void api_challeneges_challengeId_vote_투표_응답_테스트() throws Exception {
        String token = "AccessToken";
        String uuid = "uuid";

        ChallengeVoteRequest challengeVoteRequest = new ChallengeVoteRequest("uuid");
        given(jwtUtils.getUUID(token)).willReturn(uuid);
        given(memberRepository.findByUuid(uuid)).willReturn(Optional.of(member));

        //expected
        mvc.perform(post("/api/challenges/{challengeId}/vote", challenge1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeVoteRequest))
                        .with(csrf()))
                .andExpect(jsonPath("$.message").value("투표가 성공적으로 처리되었습니다."))
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
                .challengeUrl("test.com")
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