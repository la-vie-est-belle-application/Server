package lavi.scheduler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lavi.scheduler.domain.KakaoInfo;
import lavi.scheduler.domain.OAuthToken;
import lavi.scheduler.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/login/kakao")
    public RedirectView kakaoLoginPage() {

        //1. 카카오 로그인 페이지 호출 및 인가 코드 발급
        log.info("[*]   kakaoLoginPage 로 넘기기");
        String kakaoAuthURL = kakaoLoginService.getAuthorization();
        log.info("[*]   kakaoAuthURL = {}", kakaoAuthURL);

        return new RedirectView(kakaoAuthURL);
    }

    @GetMapping("/login/kakao/oauth")
    public void kakaoLogin(@RequestParam String code) throws JsonProcessingException {

        //2. 토큰 받기
        log.info("[*]   토큰 받기");
        OAuthToken oAuthToken = kakaoLoginService.getAccessToken(code);
        String accessToken = oAuthToken.getAccessToken();
        log.info("[*]   accessToken = {}", oAuthToken.getAccessToken());

        //3. 토큰 이용해서 사용자 정보 가져오기
        KakaoInfo userInfo = kakaoLoginService.getUserInfo(accessToken);

        //4. 사용자 정보의 id 이용해서 회원인지 아닌지 검증
        boolean isMember = kakaoLoginService.isMember(userInfo.getId());
        if (isMember) {
            //회원이면 로그인 성공 화면으로 이동
            log.info("[*]   로그인 성공!");
        } else {
            //회원이 아니면 회원가입 화면으로 이동(이 때 카카오 id client 로 넘겨서 회원가입할 때 포함해서 Member 객체 생성해야함)
            log.info("[*]   로그인 실패!");
        }

    }
}
