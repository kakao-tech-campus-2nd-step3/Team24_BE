package challenging.application.domain.history.controller;

import challenging.application.global.security.annotation.LoginMember;
import challenging.application.domain.auth.entity.Member;
import challenging.application.global.dto.response.HistoryResponse;
import challenging.application.domain.history.service.HistoryService;
import challenging.application.global.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryApiController {

    private final HistoryService historyService;

    @GetMapping("/{historyId}")
    public ResponseEntity<ApiResponse<?>> getHistoryOne(@LoginMember Member member,
                                                                      @PathVariable Long historyId) {
        HistoryResponse findHistory = historyService.findOneHistory(member.getId(), historyId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponse(findHistory));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getHistories(@LoginMember Member member) {
        List<HistoryResponse> histories = historyService.findAllHistory(member.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.successResponse(histories));
    }
}
