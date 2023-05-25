package pl.jbiesek.conference.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.jbiesek.conference.entites.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from _user where login=:login and email=:email", nativeQuery = true)
    Optional<User> getUserByLoginAndEmail(String login, String email);

    @Query(value = "select * from _user where login=:login", nativeQuery = true)
    Optional<User> getUserByLogin(String login);

    @Query(value = "select * from _user where email=:email", nativeQuery = true)
    Optional<User> getUserByEmail(String email);

    @Query(value = "select count(*) from _user where email=:email", nativeQuery = true)
    int checkForEmail(String email);
}
