package lavi.scheduler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lavi.scheduler.domain.KakaoInfo;
import lavi.scheduler.domain.OAuthToken;
import lavi.scheduler.domain.UserSession;
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
    public String kakaoLogin(@RequestParam String code, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        //2. 토큰 받기
        log.info("[*]   토큰 받기");
        OAuthToken oAuthToken = kakaoLoginService.getAccessToken(code);
        String accessToken = oAuthToken.getAccessToken();
        log.info("[*]   accessToken = {}", oAuthToken.getAccessToken());

        //3. 토큰 이용해서 사용자 정보 가져오기
        KakaoInfo userInfo = kakaoLoginService.getUserInfo(accessToken);

        //4. 사용자 정보의 id 이용해서 회원인지 아닌지 검증
        UserSession userSession = kakaoLoginService.isMember(userInfo.getId());
        if (userSession == null) {
            //회원이 아니면 회원가입 화면으로 이동(카카오 회원번호 같이 넘김)
            log.info("[*]   로그인 실패! 카카오 회원번호 전송 = {}", userInfo.getId());

            HttpSession httpSession = httpServletRequest.getSession();
            httpSession.setAttribute("kakaoId", userInfo.getId());

            return "redirect:/join";
        } else {
            //회원이면 홈 화면으로 이동
            log.info("[*]   로그인 성공!");

            //세션 생성
            HttpSession httpSession = httpServletRequest.getSession();
            httpSession.setAttribute("userSession", userSession);

            return "redirect:/";
        }

    }
}
