package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Entites.UserLecture;
import pl.jbiesek.conference.Respositories.LectureRepository;
import pl.jbiesek.conference.Respositories.UserLectureRepository;
import pl.jbiesek.conference.Respositories.UserRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        if(lectureRepository.findById(lecture_id).isEmpty()) {
            return 5;
        }
        if (userLectureRepository.countUsersByLectureId(lecture_id) >= 5) {
            return 4;
        }
        Optional<User> userOptional = userRepository.getUserByLoginAndEmail(login, email);
        Lecture lecture = lectureRepository.findById(lecture_id).get();
        if (userOptional.isEmpty()) {
            return 3;
        }
        User user = userOptional.get();
        if (!checkForUserAndLecture(user.getId(), lecture_id)) {
            return 2;
        }
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
    }

    @Override
    public List<Lecture> getLecturesByLogin(String login) {
        return lectureRepository.getLecturesByUserLogin(login);
    }

    @Override
    public int cancelReservation(String login, int lecture_id) {
        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isEmpty()) {
            return 3;
        }
        Optional<Lecture> lectureOptional = lectureRepository.findById(lecture_id);
        if(lectureOptional.isEmpty()){
            return 2;
        }
        int user_id = userOptional.get().getId();
        if(checkForUserAndLecture(user_id, lecture_id)) {
            return 1;
        }
        UserLecture userLecture = userLectureRepository.getByUserAndLecture(user_id, lecture_id);
        userLectureRepository.deleteById(userLecture.getId());
        return 0;
    }

    public String generateLectureReport() throws IOException {
        List<Lecture> lectures = lectureRepository.findAll();
        int numberOfReservations = userLectureRepository.countUserLectures();
        StringBuilder report = new StringBuilder();
        FileWriter fileWriter = new FileWriter("raport_prelekcje.txt");
        for(Lecture lecture : lectures){
            int numberOfUsers = userLectureRepository.countUsersByLectureId(lecture.getId());
            float percentage = ((float) numberOfUsers /numberOfReservations)*100;
            report.append(lecture.getTitle()).append(": ").append(percentage).append("%\n");
        }
        fileWriter.write(report.toString());
        fileWriter.close();
        return report.toString();
    }

    public String generateLectureReportByTheme() throws IOException {
        int numberOfReservations = userLectureRepository.countUserLectures();
        StringBuilder report = new StringBuilder();
        FileWriter fileWriter = new FileWriter("raport_sciezki_tematyczne.txt");
        List<String> themes = lectureRepository.getThemesList();
        for(String theme : themes){
            int numberOfUsers = userLectureRepository.countUserLecturesByTheme(theme);
            float percentage = ((float) numberOfUsers /numberOfReservations)*100;
            report.append(theme).append(": ").append(percentage).append("%\n");
        }
        fileWriter.write(report.toString());
        fileWriter.close();
        return report.toString();
    }
}
