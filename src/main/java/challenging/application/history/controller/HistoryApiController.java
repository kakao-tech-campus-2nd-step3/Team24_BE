package challenging.application.history.controller;

import challenging.application.auth.annotation.LoginMember;
import challenging.application.auth.domain.Member;
import challenging.application.dto.response.HistoryResponse;
import challenging.application.history.service.HistoryService;
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
    public ResponseEntity<HistoryResponse> getHistoryOne(@LoginMember Member member, @PathVariable Long historyId){
        HistoryResponse findHistory = historyService.findOneHistory(member.getId(), historyId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(findHistory);
    }

    @GetMapping
    public ResponseEntity<List<HistoryResponse>> getHistories(@LoginMember Member member){
        List<HistoryResponse> histories = historyService.findAllHistory(member.getId());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(histories);
    }

}
