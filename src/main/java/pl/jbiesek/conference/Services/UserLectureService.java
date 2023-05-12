package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.UserLecture;

import java.util.List;

public interface UserLectureService {

    public List<UserLecture> getAll();

    public Boolean add(UserLecture userLecture);

    public Boolean signUserIntoLecture(int lecture_id, String login, String email);

    public List<Lecture> getLecturesByLogin(String login);
}
