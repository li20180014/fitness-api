package rs.ac.bg.fon.FitnessPortal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.FitnessPortal.entities.Role;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ApplicationUserRole name);
}
