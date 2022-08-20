package rs.ac.bg.fon.FitnessPortal.repositories;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.FitnessPortal.entities.Member;

@Repository
public interface MemberRepository extends UserBaseRepository<Member> {
    Member findByVerificationCode(String code);
}
