package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Entites.UserLecture;
import pl.jbiesek.conference.Respositories.LectureRepository;
import pl.jbiesek.conference.Respositories.UserLectureRepository;
import pl.jbiesek.conference.Respositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserLectureServiceImplement implements UserLectureService {

    @Autowired
    UserLectureRepository userLectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Override
    public List<UserLecture> getAll() {
        return userLectureRepository.findAll();
    }

    @Override
    public Boolean add(UserLecture userLecture) {
        userLectureRepository.save(userLecture);
        return true;
    }

    public Boolean checkForUserAndLecture(int user_id, int lecture_id) {
        int result = userLectureRepository.checkByUserAndLecture(user_id, lecture_id);
        return result != 1;
    }

    @Override
    public Boolean signUserIntoLecture(int lecture_id, String login, String email) {
        if(userLectureRepository.countUsersByLectureId(lecture_id) < 5) {
            Optional<User> userOptional = userRepository.getUserByLoginAndEmail(login, email);
            Optional<Lecture> lectureOptional = lectureRepository.findById(lecture_id);
            if (userOptional.isPresent() && lectureOptional.isPresent()) {
                User user = userOptional.get();
                Lecture lecture = lectureOptional.get();
                System.out.println(checkForUserAndLecture(user.getId(), lecture_id));
                if (!checkForUserAndLecture(user.getId(), lecture_id)) {
                    userLectureRepository.save(new UserLecture(user, lecture));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Lecture> getLecturesByLogin(String login) {
        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isPresent()){
            int user_id = userOptional.get().getId();
            return lectureRepository.getLecturesByUserId(user_id);
        }
        return new ArrayList<>();
    }
}
