package pl.jbiesek.conference.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jbiesek.conference.Entites.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
