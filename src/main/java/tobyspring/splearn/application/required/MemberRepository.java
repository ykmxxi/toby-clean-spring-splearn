package tobyspring.splearn.application.required;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;

/**
 * 회원 정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> {

    /**
     * save: 새로운 엔티티 오브젝트를 생성, 영속화할 때나 준영속(detached) 상태를 merge 할 때 사용하는 것이 관례
     * 그런데 Spring Data JPA에서는 어떤 데이터를 수정할 때 호출하는 관례가 있음
     * JPA가 아닌 Spring Data JPA를 사용할 때는 등록과 수정에 모두 save를 호출하는 것이 좋음
     */
    Member save(Member member);

    Optional<Member> findByEmail(Email email);
}
