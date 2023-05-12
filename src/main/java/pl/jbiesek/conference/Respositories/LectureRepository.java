package pl.jbiesek.conference.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jbiesek.conference.Entites.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
}
