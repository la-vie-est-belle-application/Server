package lavi.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lavi.scheduler.domain.KakaoInfo;
import lavi.scheduler.domain.Member;
import lavi.scheduler.domain.OAuthToken;
import lavi.scheduler.domain.UserSession;
import lavi.scheduler.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final String clientId;
    private final String redirectURI;

    public KakaoLoginService(MemberRepository memberRepository,
                             @Value("${kakao.client_id}") String clientId,
                             @Value("${kakao.redirect_uri}") String redirectURI) {
        this.memberRepository = memberRepository;
        this.clientId = clientId;
        this.redirectURI = redirectURI;
    }

    public String getAuthorization() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectURI;
    }

    public OAuthToken getAccessToken(String code) throws JsonProcessingException {
        log.info("[*]   토큰 가져오기, code = {}", code);
        String urlStr = "https://kauth.kakao.com/oauth/token";

        RestTemplate rt = new RestTemplate();

        //헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //바디 생성(하나의 키 값에 여러개의 데이터를 가질 수 있는 MultiValueMap 사용)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectURI);
        params.add("code", code);

        //HttpEntity 이용해 헤더, 바디 가진 Http 엔티티 생성
        log.info("[*]   헤더, 바디 가진 httpEntity 생성");
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //rt 이용해서 post 방식으로 request 넣어서 전송, 결과 string type 으로 반환
        log.info("[*]   Post 방식으로 Http 요청 및 결과 반환");
        ResponseEntity<String> response = rt.exchange(urlStr, HttpMethod.POST, kakaoTokenRequest, String.class);

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, OAuthToken.class);
    }

    public KakaoInfo getUserInfo(String accessToken) throws JsonProcessingException {

        log.info("[*]   Access Token 이용해서 사용자 정보 가져오기");
        String urlStr = "https://kapi.kakao.com/v2/user/me";

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        return rt.exchange(urlStr, HttpMethod.GET, userInfoRequest, KakaoInfo.class).getBody();
    }

    public UserSession isMember(String id) {
        log.info("[*]   데이터베이스에서 카카오 고유 id와 일치하는 값이 있는지 검증");
        Member member = memberRepository.findByKakaoId(id);
        if (member == null) {
            return null;
        } else {
            return new UserSession(member.getId(), member.getName(), member.getRollType());
        }
    }
}
