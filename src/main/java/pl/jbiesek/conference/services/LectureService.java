package pl.jbiesek.conference.services;

import pl.jbiesek.conference.entites.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureService {

    public List<Lecture> getAll();

    public Optional<Lecture> getById(int id);

    public Boolean add(Lecture lecture);

    public Boolean update(int id, Lecture updatedLecture);

    public Boolean delete(int id);
}
