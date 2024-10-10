package challenging.application.challenge.controller;

import challenging.application.auth.annotation.LoginMember;
import challenging.application.auth.domain.Member;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.request.DateRequest;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.challenge.service.ChallengeService;

import java.util.List;

import challenging.application.dto.response.ReserveChallengeResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/challenges")
public class ChallengeController {

  private final ChallengeService challengeService;

  public ChallengeController(ChallengeService challengeService) {
    this.challengeService = challengeService;
  }

  // 챌린지 단건 조회
  @GetMapping("/{challengeId}")
  public ResponseEntity<ChallengeResponse> getChallenge(
      @PathVariable Long challengeId,
      @RequestBody DateRequest dateRequest) {

    ChallengeResponse response = challengeService.getChallengeByIdAndDate(challengeId,
        dateRequest.date());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 챌린지 카테고리 조회
  @GetMapping("/{categoryId}")
  public ResponseEntity<List<ChallengeResponse>> getChallengesByCategory(
      @PathVariable int categoryId,
      @RequestBody DateRequest dateRequest) {

    List<ChallengeResponse> responses = challengeService.getChallengesByCategoryAndDate(
        categoryId, dateRequest.date());

    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  // 챌린지 생성
  @PostMapping
  public ResponseEntity<Long> createChallenge(
      @RequestBody ChallengeRequest challengeRequestDTO) {

    Long challengeId = challengeService.createChallenge(challengeRequestDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(challengeId);
  }

  // 챌린지 삭제
  @DeleteMapping("{challengeId}")
  public ResponseEntity<Long> deleteChallenge(@PathVariable Long challengeId,
      @LoginMember Member loginMember) {
    challengeService.deleteChallenge(challengeId, loginMember);
    return ResponseEntity.status(HttpStatus.OK).body(challengeId);
  }

  // 챌린지 예약
  @PostMapping("/reservation/{challengeId}")
  public ResponseEntity<?> reserveChallenge(@PathVariable Long challengeId,
      @LoginMember Member loginMember) {
    challengeService.reserveChallenge(challengeId, loginMember);

    ReserveChallengeResponse response = new ReserveChallengeResponse(challengeId,
        loginMember.getId());

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
  }
}