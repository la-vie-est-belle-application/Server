package lavi.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lavi.scheduler.domain.Member;
import lavi.scheduler.domain.ResponseDto;
import lavi.scheduler.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> join(@RequestBody MemberDto memberDto, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) {
            log.info("[*]   세션 없음 카카오 로그인 화면으로 이동");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDto<>("카카오 로그인 필요", false));
        } else {
            Member member = new Member(memberDto.kakaoId, memberDto.name, memberDto.eMail, memberDto.gender);
            Member savedMember = memberService.saveMember(member);
            if (savedMember == null) {
                log.info("[*]   회원 가입 실패");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDto<>("회원 정보 저장 실패", false));
            }
        }
        log.info("[*]   회원 가입 성공");
        return ResponseEntity.ok()
                .body(new ResponseDto<>("회원 가입 성공", true));
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String kakaoId;
        private String name;
        private String eMail;
        private String gender;
    }


}
