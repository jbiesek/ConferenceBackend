package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.UserLecture;

import java.io.IOException;
import java.util.List;

public interface UserLectureService {

    public List<UserLecture> getAll();

    public Boolean add(UserLecture userLecture);

    public int signUserIntoLecture(int lecture_id, String login, String email) throws IOException;

    public List<Lecture> getLecturesByLogin(String login);

    public int cancelReservation(String login, int lecture_id);
}
