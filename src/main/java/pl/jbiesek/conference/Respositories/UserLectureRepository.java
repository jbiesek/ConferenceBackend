package pl.jbiesek.conference.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jbiesek.conference.Entites.UserLecture;

public interface UserLectureRepository extends JpaRepository<UserLecture, Integer> {

    @Query(value = "select count(*) from _user_lecture where lecture_id = :lecture_id", nativeQuery = true)
    int countUsersByLectureId(int lecture_id);

    @Query(value = "select count(*) from _user_lecture where user_id=:user_id and lecture_id=:lecture_id", nativeQuery = true)
    int checkByUserAndLecture(int user_id, int lecture_id);

    @Query(value = "select * from _user_lecture where user_id=:user_id and lecture_id=:lecture_id", nativeQuery = true)
    UserLecture getByUserAndLecture(int user_id, int lecture_id);

}
