package challenging.application.challenge;

import challenging.application.auth.domain.Member;
import challenging.application.challenge.controller.ChallengeController;
import challenging.application.challenge.service.ChallengeService;
import challenging.application.domain.Category;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.request.DateRequest;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.dto.response.ReserveChallengeResponse;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChallengeControllerTest {

  @InjectMocks
  private ChallengeController challengeController;

  @Mock
  private ChallengeService challengeService;

  private AutoCloseable closeable;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    closeable.close();
  }


  @Test
  void getChallenge() {
    Long challengeId = 1L;
    DateRequest dateRequest = new DateRequest("2024-10-10 12:00:00");

    ChallengeResponse mockResponse = new ChallengeResponse("Test Challenge", "Description", 100,
        "2024-10-10", "12:00:00", "14:00:00",
        "https://health.chosun.com/site/data/img_dir/2022/03/30/2022033000861_0.jpg", 5, 10, 3, 1L,
        "sports");
    when(challengeService.getChallengeByIdAndDate(challengeId, dateRequest.date())).thenReturn(
        mockResponse);

    ResponseEntity<ChallengeResponse> responseEntity = challengeController.getChallenge(challengeId,
        dateRequest);

    assertEquals(201, responseEntity.getStatusCode().value());
    assertEquals(mockResponse, responseEntity.getBody());
  }

  @Test
  void getChallengesByCategory() {
    String category = "sports";
    DateRequest dateRequest = new DateRequest("2024-10-10 12:00:00");

    List<ChallengeResponse> mockResponseList = List.of(
        new ChallengeResponse("Test Challenge", "Description", 100, "2024-10-10", "12:00:00",
            "14:00:00",
            "https://health.chosun.com/site/data/img_dir/2022/03/30/2022033000861_0.jpg", 5, 10, 3,
            1L, category));
    when(challengeService.getChallengesByCategoryAndDate(category, dateRequest.date())).thenReturn(
        mockResponseList);

    ResponseEntity<List<ChallengeResponse>> responseEntity = challengeController.getChallengesByCategory(
        category, dateRequest);

    assertEquals(200, responseEntity.getStatusCode().value());
    assertEquals(mockResponseList, responseEntity.getBody());
  }

  @Test
  void createChallenge() {
    ChallengeRequest challengeRequest = new ChallengeRequest(1L, Category.SPORTS, "Test Challenge",
        "Description", 100, "2024-10-10", "12:00:00", "14:00:00",
        "https://health.chosun.com/site/data/img_dir/2022/03/30/2022033000861_0.jpg", 5, 10);
    Long mockChallengeId = 1L;
    when(challengeService.createChallenge(challengeRequest)).thenReturn(mockChallengeId);

    ResponseEntity<Long> responseEntity = challengeController.createChallenge(challengeRequest);

    assertEquals(201, responseEntity.getStatusCode().value());
    assertEquals(mockChallengeId, responseEntity.getBody());
  }

  @Test
  void deleteChallenge() {
    Long challengeId = 1L;

    doNothing().when(challengeService).deleteChallenge(challengeId);

    ResponseEntity<Long> responseEntity = challengeController.deleteChallenge(challengeId);

    assertEquals(200, responseEntity.getStatusCode().value());
    assertEquals(challengeId, responseEntity.getBody());
  }

  @Test
  void reserveChallenge() {
    Long challengeId = 1L;
    Member loginMember = new Member("username", "nickName", "email@example.com", "ROLE_USER");

    doNothing().when(challengeService).reserveChallenge(challengeId, loginMember);

    ReserveChallengeResponse expectedResponse = new ReserveChallengeResponse(challengeId,
        loginMember.getId());

    ResponseEntity<?> responseEntity = challengeController.reserveChallenge(challengeId,
        loginMember);

    assertEquals(200, responseEntity.getStatusCode().value());
    assertEquals(expectedResponse, responseEntity.getBody());
  }
}
