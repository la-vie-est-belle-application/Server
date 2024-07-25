package lavi.scheduler.service;

import lavi.scheduler.domain.Member;
import lavi.scheduler.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        log.info("[*]   회원 정보 저장");
        Member savedMember = memberRepository.save(member);
        if (savedMember == null) {
            log.info("[*]   데이터베이스 저장 실패");
            return null;
        } else {
            return savedMember;
        }
    }

}
