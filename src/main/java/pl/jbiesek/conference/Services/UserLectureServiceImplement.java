package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Entites.UserLecture;
import pl.jbiesek.conference.Respositories.LectureRepository;
import pl.jbiesek.conference.Respositories.UserLectureRepository;
import pl.jbiesek.conference.Respositories.UserRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    public int signUserIntoLecture(int lecture_id, String login, String email) throws IOException {
        if(lectureRepository.findById(lecture_id).isPresent()) {
            if (userLectureRepository.countUsersByLectureId(lecture_id) < 5) {
                Optional<User> userOptional = userRepository.getUserByLoginAndEmail(login, email);
                Lecture lecture = lectureRepository.findById(lecture_id).get();
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (checkForUserAndLecture(user.getId(), lecture_id)) {
                        List<Lecture> userLectures = lectureRepository.getLecturesByUserId(user.getId());
                        for(Lecture l : userLectures) {
                            if(l.getDate().equals(lecture.getDate())){
                                return 1;
                            }
                        }
                        userLectureRepository.save(new UserLecture(user, lecture));
                        FileWriter fileWriter = new FileWriter("powiadomienia.txt",true);
                        Date date = new Date();
                        fileWriter.write("Data: " + date + "\n");
                        fileWriter.write("Wiadomość do: " + user.getEmail() + "\n\n");
                        fileWriter.write("Cześć " + user.getLogin() + "!\n\n" + "Zarejestrowałeś/łaś się na prelekcję pt. "
                                + lecture.getTitle() + " ze ścieżki tematycznej " + lecture.getTheme()
                                + ". Prelekcja odbędzie się dnia " + DateTimeFormatter.ofPattern("dd.MM.yyyy ").format(lecture.getDate())
                                + "o godzinie " + DateTimeFormatter.ofPattern("HH:mm").format(lecture.getDate())
                                + ".\n\n" + "Pozdrawiamy,\n" + "Organizatorzy" + "\n\n---------------------------\n\n");
                        fileWriter.close();
                        return 0;
                    } else {
                        return 2;
                    }
                } else {
                    return 3;
                }
            } else {
                return 4;
            }
        } else {
            return 5;
        }
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

    @Override
    public int cancelReservation(String login, int lecture_id) {
        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isPresent()) {
            Optional<Lecture> lectureOptional = lectureRepository.findById(lecture_id);
            if(lectureOptional.isPresent()){
                int user_id = userOptional.get().getId();
                if(!checkForUserAndLecture(user_id, lecture_id)) {
                    UserLecture userLecture = userLectureRepository.getByUserAndLecture(user_id, lecture_id);
                    userLectureRepository.deleteById(userLecture.getId());
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}
