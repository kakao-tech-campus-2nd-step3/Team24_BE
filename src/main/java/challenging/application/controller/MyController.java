package challenging.application.controller;

import challenging.application.auth.annotation.LoginMember;
import challenging.application.auth.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MyController {

    @GetMapping("/my")
    public String myAPI(@LoginMember Member member) {
        log.info("member = {}",member);
        log.info("home controller name = {}", member.getUsername());
        return "my route";
    }
}
