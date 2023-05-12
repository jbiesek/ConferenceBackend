package pl.jbiesek.conference.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jbiesek.conference.Entites.UserLecture;

public interface UserLectureRepository extends JpaRepository<UserLecture, Integer> {
}
