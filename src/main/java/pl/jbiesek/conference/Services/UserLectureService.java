package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Entites.UserLecture;

import java.util.List;

public interface UserLectureService {

    public List<UserLecture> getAll();

    public Boolean add(UserLecture userLecture);
}
