package challenging.application.challenge.controller;

import challenging.application.auth.annotation.LoginMember;
import challenging.application.auth.domain.Member;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.request.DateRequest;
import challenging.application.dto.response.ChallengeCreateResponse;
import challenging.application.dto.response.ChallengeDeleteResponse;
import challenging.application.dto.response.ChallengeReservationResponse;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.challenge.service.ChallengeService;

import challenging.application.util.response.ApiResponse;
import java.util.List;

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
    public ResponseEntity<ApiResponse<ChallengeResponse>> getChallenge(
            @PathVariable Long challengeId) {

        ChallengeResponse response = challengeService.getChallengeById(challengeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("success", 200, "챌린지 조회가 완료되었습니다.", response));
    }

    // 챌린지 카테고리 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getChallengesByCategory(
            @PathVariable int categoryId,
            @RequestBody DateRequest dateRequest) {

        List<ChallengeResponse> responses = challengeService.getChallengesByCategoryAndDate(
                categoryId, dateRequest.date());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("success", 200, "카테고리별 챌린지 조회가 완료되었습니다.", responses));
    }

    // 챌린지 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ChallengeCreateResponse>> createChallenge(
            @RequestBody ChallengeRequest challengeRequestDTO) {

        ChallengeCreateResponse response = challengeService.createChallenge(challengeRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", 201, "챌린지 생성이 완료되었습니다.", response));
    }

    // 챌린지 삭제
    @DeleteMapping("{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeDeleteResponse>> deleteChallenge(
            @PathVariable Long challengeId,
            @LoginMember Member loginMember
    ) {
        ChallengeDeleteResponse response = challengeService.deleteChallenge(challengeId, loginMember);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("success", 200, "챌린지 삭제가 완료되었습니다.", response));
    }

    // 챌린지 예약
    @PostMapping("/reservation/{challengeId}")
    public ResponseEntity<ApiResponse<?>> reserveChallenge(
            @PathVariable Long challengeId,
            @LoginMember Member loginMember
    ) {
        ChallengeReservationResponse challengeResponse = challengeService.reserveChallenge(challengeId, loginMember);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>("success", 200, "챌린지 예약이 완료되었습니다.", challengeResponse));
    }
}
