package pl.jbiesek.conference.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Entites.UserLecture;
import pl.jbiesek.conference.Services.LectureService;
import pl.jbiesek.conference.Services.UserLectureService;
import pl.jbiesek.conference.Services.UserService;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api")
public class IndexController {

    @Autowired
    UserService userService;

    @Autowired
    LectureService lectureService;

    @Autowired
    UserLectureService userLectureService;

    @PostMapping("/generateModel")
    public ResponseEntity<Void> generateModel() {

        User u0 = new User("Jan", "jan_kowalski@gmail.com");
        User u1 = new User("Ania", "anna_malinowska@gmail.com");
        User u2 = new User("Marek", "marek_adamski@gmail.com");
        User u3 = new User("Basia", "barbara_andrzejewska@gmail.com");
        User u4 = new User("Adam", "adam_nowak@gmail.com");
        User u5 = new User("Alicja", "alicja_domagala@gmail.com");
        User u6 = new User("Michal", "michal_wisniewski@gmail.com");
        User u7 = new User("Weronika", "weronika_borkowska@gmail.com");
        User u8 = new User("Stefan", "stefan_siarzewski@gmail.com");
        User u9 = new User("Dominika", "dominika_maklowicz@gmail.com");

        ZoneId zoneId = ZonedDateTime.now().getZone();
        Lecture l0 = new Lecture("Podstawy Spring Boota", "Backend", ZonedDateTime.of(2023, 6, 1, 10, 0, 0, 0, zoneId));
        Lecture l1 = new Lecture("Ataki SQL injection", "Cyberbezpieczeństwo", ZonedDateTime.of(2023, 6, 1, 10, 0, 0, 0, zoneId));
        Lecture l2 = new Lecture("Docker dla początkujących", "DevOps", ZonedDateTime.of(2023, 6, 1, 10, 0, 0, 0, zoneId));
        Lecture l3 = new Lecture("Zaawansowany Spring Boot", "Backend", ZonedDateTime.of(2023, 6, 1, 12, 0, 0, 0, zoneId));
        Lecture l4 = new Lecture("Phishing", "Cyberbezpieczeństwo", ZonedDateTime.of(2023, 6, 1, 12, 0, 0, 0, zoneId));
        Lecture l5 = new Lecture("Wstęp do AWS", "DevOps", ZonedDateTime.of(2023, 6, 1, 12, 0, 0, 0, zoneId));
        Lecture l6 = new Lecture("Dobre praktyki w Javie", "Backend", ZonedDateTime.of(2023, 6, 1, 14, 0, 0, 0, zoneId));
        Lecture l7 = new Lecture("Kryptografia asymetryczna", "Cyberbezpieczeństwo", ZonedDateTime.of(2023, 6, 1, 14, 0, 0, 0, zoneId));
        Lecture l8 = new Lecture("Linux dla początkujących", "DevOps", ZonedDateTime.of(2023, 6, 1, 14, 0, 0, 0, zoneId));

        UserLecture ul0 = new UserLecture(u0, l0);
        UserLecture ul1 = new UserLecture(u0, l3);
        UserLecture ul2 = new UserLecture(u0, l6);
        UserLecture ul3 = new UserLecture(u1, l1);
        UserLecture ul4 = new UserLecture(u1, l4);
        UserLecture ul5 = new UserLecture(u1, l7);
        UserLecture ul6 = new UserLecture(u2, l2);
        UserLecture ul7 = new UserLecture(u2, l5);
        UserLecture ul8 = new UserLecture(u2, l8);
        UserLecture ul9 = new UserLecture(u3, l0);
        UserLecture ul10 = new UserLecture(u3, l4);
        UserLecture ul11 = new UserLecture(u3, l8);
        UserLecture ul12 = new UserLecture(u4, l2);
        UserLecture ul13 = new UserLecture(u4, l5);
        UserLecture ul14 = new UserLecture(u4, l6);
        UserLecture ul15 = new UserLecture(u5, l1);
        UserLecture ul16 = new UserLecture(u6, l0);
        UserLecture ul17 = new UserLecture(u6, l5);
        UserLecture ul18 = new UserLecture(u7, l0);
        UserLecture ul19 = new UserLecture(u7, l8);
        UserLecture ul20 = new UserLecture(u8, l0);
        UserLecture ul21 = new UserLecture(u8, l6);
        UserLecture ul22 = new UserLecture(u9, l2);
        UserLecture ul23 = new UserLecture(u9, l3);
        UserLecture ul24 = new UserLecture(u9, l7);

        userService.add(u0);
        userService.add(u1);
        userService.add(u2);
        userService.add(u3);
        userService.add(u4);
        userService.add(u5);
        userService.add(u6);
        userService.add(u7);
        userService.add(u8);
        userService.add(u9);

        lectureService.add(l0);
        lectureService.add(l1);
        lectureService.add(l2);
        lectureService.add(l3);
        lectureService.add(l4);
        lectureService.add(l5);
        lectureService.add(l6);
        lectureService.add(l7);
        lectureService.add(l8);

        userLectureService.add(ul0);
        userLectureService.add(ul1);
        userLectureService.add(ul2);
        userLectureService.add(ul3);
        userLectureService.add(ul4);
        userLectureService.add(ul5);
        userLectureService.add(ul6);
        userLectureService.add(ul7);
        userLectureService.add(ul8);
        userLectureService.add(ul9);
        userLectureService.add(ul10);
        userLectureService.add(ul11);
        userLectureService.add(ul12);
        userLectureService.add(ul13);
        userLectureService.add(ul14);
        userLectureService.add(ul15);
        userLectureService.add(ul16);
        userLectureService.add(ul17);
        userLectureService.add(ul18);
        userLectureService.add(ul19);
        userLectureService.add(ul20);
        userLectureService.add(ul21);
        userLectureService.add(ul22);
        userLectureService.add(ul23);
        userLectureService.add(ul24);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
