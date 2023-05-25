package pl.jbiesek.conference.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.jbiesek.conference.entites.UserLecture;

@Repository
public interface UserLectureRepository extends JpaRepository<UserLecture, Integer> {

    @Query(value = "select count(*) from _user_lecture where lecture_id = :lecture_id", nativeQuery = true)
    int countUsersByLectureId(int lecture_id);

    @Query(value = "select count(*) from _user_lecture where user_id=:user_id and lecture_id=:lecture_id", nativeQuery = true)
    int checkByUserAndLecture(int user_id, int lecture_id);

    @Query(value = "select * from _user_lecture where user_id=:user_id and lecture_id=:lecture_id", nativeQuery = true)
    UserLecture getByUserAndLecture(int user_id, int lecture_id);

    @Query(value = "select count(*) from _user_lecture", nativeQuery = true)
    int countUserLectures();

    @Query(value = "select COUNT(*) from _user_lecture ul join _lecture l on ul.lecture_id = l.id where l.theme=:theme", nativeQuery = true)
    int countUserLecturesByTheme(String theme);

}
