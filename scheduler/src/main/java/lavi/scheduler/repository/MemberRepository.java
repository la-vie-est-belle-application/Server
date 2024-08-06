package lavi.scheduler.repository;

import lavi.scheduler.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByKakaoId(String id);

    List<Member> findByIdIn(List<Long> ids);
}
