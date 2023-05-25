package pl.jbiesek.conference.services;

import org.springframework.stereotype.Service;
import pl.jbiesek.conference.entites.Lecture;
import pl.jbiesek.conference.entites.User;
import pl.jbiesek.conference.entites.UserLecture;
import pl.jbiesek.conference.responses.MessageResponse;
import pl.jbiesek.conference.respositories.LectureRepository;
import pl.jbiesek.conference.respositories.UserLectureRepository;
import pl.jbiesek.conference.respositories.UserRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserLectureServiceImplement implements UserLectureService {

    private final UserLectureRepository userLectureRepository;

    private final UserRepository userRepository;

    private final LectureRepository lectureRepository;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public UserLectureServiceImplement(UserLectureRepository userLectureRepository, UserRepository userRepository, LectureRepository lectureRepository) {
        this.userLectureRepository = userLectureRepository;
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public List<UserLecture> getAll() {
        return userLectureRepository.findAll();
    }

    @Override
    public Boolean add(UserLecture userLecture) {
        userLectureRepository.save(userLecture);
        return true;
    }

    @Override
    public MessageResponse signUserIntoLecture(int lecture_id, String login, String email) throws IOException {
        if(lectureRepository.findById(lecture_id).isEmpty()) {
            return MessageResponse.builder().message("Podana prelekcja nie istnieje.").success(false).build();
        }
        if (userLectureRepository.countUsersByLectureId(lecture_id) >= 5) {
            return MessageResponse.builder().message("Na podaną prelekcję zapisana jest maksymalna ilość osób.").success(false).build();
        }
        Optional<User> userOptional = userRepository.getUserByLoginAndEmail(login, email);
        Lecture lecture = lectureRepository.findById(lecture_id).get();
        if (userOptional.isEmpty()) {
            return MessageResponse.builder().message("Podany użytkownik nie istnieje.").success(false).build();
        }
        User user = userOptional.get();
        if (!checkForUserAndLecture(user.getId(), lecture_id)) {
            return MessageResponse.builder().message("Podany użytkownik jest już zapisany na podaną prelekcję.").success(false).build();
        }
        List<Lecture> userLectures = lectureRepository.getLecturesByUserId(user.getId());
        for(Lecture l : userLectures) {
            if(l.getDate().equals(lecture.getDate())){
                return MessageResponse.builder().message("Podany użytkownik jest już zapisany na inną prelekcję o tej samej godzinie.").success(false).build();
            }
        }
        userLectureRepository.save(new UserLecture(user, lecture));
        FileWriter fileWriter = new FileWriter("powiadomienia.txt",true);
        Date date = new Date();
        try {
            fileWriter.write("Data: " + date + "\n");
            fileWriter.write("Wiadomość do: " + user.getEmail() + "\n\n");
            fileWriter.write("Cześć " + user.getLogin() + "!\n\n" + "Zarejestrowałeś/łaś się na prelekcję pt. "
                    + lecture.getTitle() + " ze ścieżki tematycznej " + lecture.getTheme()
                    + ". Prelekcja odbędzie się dnia " + DateTimeFormatter.ofPattern("dd.MM.yyyy ").format(lecture.getDate())
                    + "o godzinie " + DateTimeFormatter.ofPattern("HH:mm").format(lecture.getDate())
                    + ".\n\n" + "Pozdrawiamy,\n" + "Organizatorzy" + "\n\n---------------------------\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }
        return MessageResponse.builder().message("Pomyślnie dokonano zapisu.").success(true).build();
    }

    @Override
    public List<Lecture> getLecturesByLogin(String login) {
        return lectureRepository.getLecturesByUserLogin(login);
    }

    @Override
    public MessageResponse cancelReservation(String login, int lecture_id) {
        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isEmpty()) {
            return MessageResponse.builder().message("Podany użytkownik nie istnieje.").success(false).build();
        }
        Optional<Lecture> lectureOptional = lectureRepository.findById(lecture_id);
        if(lectureOptional.isEmpty()){
            return MessageResponse.builder().message("Podana prelekcja nie istnieje.").success(false).build();
        }
        int user_id = userOptional.get().getId();
        if(checkForUserAndLecture(user_id, lecture_id)) {
            return MessageResponse.builder().message("Podany użytkownik nie zarezerwował miejsca na podanej prelekcji.").success(false).build();
        }
        UserLecture userLecture = userLectureRepository.getByUserAndLecture(user_id, lecture_id);
        userLectureRepository.deleteById(userLecture.getId());
        return MessageResponse.builder().message("Pomyślnie anulowano rezerwację.").success(true).build();
    }

    @Override
    public String generateLectureReport() throws IOException {
        List<Lecture> lectures = lectureRepository.findAll();
        int numberOfReservations = userLectureRepository.countUserLectures();
        StringBuilder report = new StringBuilder();
        FileWriter fileWriter = new FileWriter("raport_prelekcje.txt");
        lectures.forEach(lecture -> {
            int numberOfUsers = userLectureRepository.countUsersByLectureId(lecture.getId());
            String percentage = df.format(((float) numberOfUsers /numberOfReservations)*100);
            report.append(lecture.getTitle()).append(": ").append(percentage).append("%\n");});
        fileWriter.write(report.toString());
        fileWriter.close();
        return report.toString();
    }

    @Override
    public String generateLectureReportByTheme() throws IOException {
        int numberOfReservations = userLectureRepository.countUserLectures();
        StringBuilder report = new StringBuilder();
        FileWriter fileWriter = new FileWriter("raport_sciezki_tematyczne.txt");
        List<String> themes = lectureRepository.getThemesList();
        themes.forEach(theme -> {
            int numberOfUsers = userLectureRepository.countUserLecturesByTheme(theme);
            String percentage = df.format(((float) numberOfUsers /numberOfReservations)*100);
            report.append(theme).append(": ").append(percentage).append("%\n");
        });
        fileWriter.write(report.toString());
        fileWriter.close();
        return report.toString();
    }

    private Boolean checkForUserAndLecture(int user_id, int lecture_id) {
        int result = userLectureRepository.checkByUserAndLecture(user_id, lecture_id);
        return result == 0;
    }
}
