package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.UserLecture;
import pl.jbiesek.conference.Responses.MessageResponse;

import java.io.IOException;
import java.util.List;

public interface UserLectureService {

    public List<UserLecture> getAll();

    public Boolean add(UserLecture userLecture);

    public MessageResponse signUserIntoLecture(int lecture_id, String login, String email) throws IOException;

    public List<Lecture> getLecturesByLogin(String login);

    public MessageResponse cancelReservation(String login, int lecture_id);

    public String generateLectureReport() throws IOException;

    public String generateLectureReportByTheme() throws IOException;
}
