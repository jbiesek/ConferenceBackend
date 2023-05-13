package pl.jbiesek.conference.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jbiesek.conference.Entites.Lecture;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    @Query(value = "select l.* from _lecture l join _user_lecture ul on l.id = ul.lecture_id where ul.user_id=:user_id", nativeQuery = true)
    List<Lecture> getLecturesByUserId(int user_id);

    @Query(value = "select l.* from _lecture l join _user_lecture ul on l.id = ul.lecture_id join _user u on u.id = ul.user_id where u.login = :login", nativeQuery = true)
    List<Lecture> getLecturesByUserLogin(String login);

    @Query(value = "select distinct(theme) from _lecture", nativeQuery = true)
    List<String> getThemesList();
}
