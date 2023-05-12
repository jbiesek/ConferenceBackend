package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Respositories.LectureRepository;

import java.util.List;

@Service
public class LectureServiceImplement implements LectureService {
    @Autowired
    LectureRepository lectureRepository;

    @Override
    public List<Lecture> getAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Lecture getById(int id) {
        if(lectureRepository.findById(id).isPresent()){
            return lectureRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Boolean add(Lecture lecture) {
        if (lecture.getTitle()!=null && lecture.getTheme()!=null && lecture.getDate()!=null) {
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
        if(updatedLecture.getTitle() != null) {
            lecture.setTitle(updatedLecture.getTitle());
        }
        if(updatedLecture.getTheme() != null) {
            lecture.setTheme(updatedLecture.getTheme());
        }
        if(updatedLecture.getDate() != null) {
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
