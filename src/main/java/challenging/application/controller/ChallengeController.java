package challenging.application.controller;


import challenging.application.domain.Challenge;
import challenging.application.dto.response.ChallengeResponseDTO;
import challenging.application.exception.ErrorResponse;
import challenging.application.service.ChallengeService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/challenges")
public class ChallengeController {

  private final ChallengeService challengeService;

  public ChallengeController(ChallengeService challengeService) {
    this.challengeService = challengeService;
  }

  // 챌린지 단건 조회
  public ResponseEntity<?> getChallenge(
      @PathVariable Long challengeId,
      @RequestBody Map<String, String> requestBody) {

    try {
      ChallengeResponseDTO response = challengeService.getChallengeByIdAndDate(challengeId,
          requestBody.get("date"));
      return ResponseEntity.status(201).body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(400).body(new ErrorResponse(400, e.getMessage()));
    }
  }

  // 챌린지 카테고리 조회

  // 챌린지 생성

  // 챌린지 삭제

  // 챌린지 예약


}
