package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Respositories.LectureRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImplement implements LectureService {
    private final LectureRepository lectureRepository;

    public LectureServiceImplement(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public List<Lecture> getAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Optional<Lecture> getById(int id) {
        return lectureRepository.findById(id);
    }

    @Override
    public Boolean add(Lecture lecture) {
        if (!lecture.getTitle().isEmpty() && !lecture.getTheme().isEmpty() && lecture.getDate()!=null) {
            lectureRepository.save(lecture);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean update(int id, Lecture updatedLecture) {
        if(!lectureRepository.existsById(id)){
            return false;
        }
        Lecture lecture = lectureRepository.getReferenceById(id);
        if(!updatedLecture.getTitle().isEmpty() && !updatedLecture.getTitle().equals(lecture.getTitle())) {
            lecture.setTitle(updatedLecture.getTitle());
        }
        if(!updatedLecture.getTheme().isEmpty() && !updatedLecture.getTheme().equals(lecture.getTheme())) {
            lecture.setTheme(updatedLecture.getTheme());
        }
        if(updatedLecture.getDate() != null && !updatedLecture.getDate().equals(lecture.getDate())) {
            lecture.setDate(updatedLecture.getDate());
        }
        try {
            lectureRepository.save(lecture);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Boolean delete(int id) {
        if(lectureRepository.existsById(id)){
            lectureRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
