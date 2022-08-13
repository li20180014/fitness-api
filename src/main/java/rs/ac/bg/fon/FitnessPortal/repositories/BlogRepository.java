package rs.ac.bg.fon.FitnessPortal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.FitnessPortal.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
}
