package pl.jbiesek.conference.Controller;

import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Services.LectureService;
import pl.jbiesek.conference.Services.UserLectureService;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LectureController {

    @Autowired
    LectureService lectureService;

    @Autowired
    UserLectureService userLectureService;

    @GetMapping("/lectures/sortByDate")
    public List<Lecture> getAll() {
        List<Lecture> lectures = lectureService.getAll();
        lectures.sort(Comparator.comparing(Lecture::getDate));
        return lectures;
    }

    @GetMapping("/lectures/sortByTheme")
    public List<Lecture> getAllByTheme() {
        List<Lecture> lectures = lectureService.getAll();
        lectures.sort(Comparator.comparing(Lecture::getTheme));
        return lectures;
    }

    @GetMapping("/lecture/{id}")
    public Lecture getById(@PathVariable("id") int id) {
        return lectureService.getById(id);
    }

    @GetMapping("/lectures/byLogin")
    public List<Lecture> getLecturesByLogin(@RequestBody String login) {
        List<Lecture> lectures = userLectureService.getLecturesByLogin(login);
        lectures.sort(Comparator.comparing(Lecture::getDate));
        return lectures;
    }

    @PostMapping("/lecture")
    public ResponseEntity<Void> add(@RequestBody Lecture lecture) {
        if(lectureService.add(lecture)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/lecture/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody Lecture updatedLecture) {
        if (lectureService.update(id, updatedLecture)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/lecture/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        if (lectureService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/lecture/signIn/{lecture_id}")
    public ResponseEntity<String> signIn (@PathVariable("lecture_id") int lecture_id, @RequestBody User user) throws IOException {
        int serviceResponse = userLectureService.signUserIntoLecture(lecture_id, user.getLogin(), user.getEmail());
        if(serviceResponse == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (serviceResponse == 1) {
            return new ResponseEntity<>("Podany użytkownik jest już zapisany na inną prelekcję o tej samej godzinie.",HttpStatus.FORBIDDEN);
        } else if (serviceResponse == 2) {
            return new ResponseEntity<>("Podany użytkownik jest już zapisany na podaną prelekcję.",HttpStatus.FORBIDDEN);
        } else if (serviceResponse == 3) {
            return new ResponseEntity<>("Podany użytkownik nie istnieje.", HttpStatus.FORBIDDEN);
        } else if (serviceResponse == 4) {
            return new ResponseEntity<>("Na podaną prelekcję zapisana jest maksymalna ilość osób.", HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Podana prelekcja nie istnieje.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/lecture/cancelReservation/{lecture_id}")
    public ResponseEntity<String> cancelReservation(@PathVariable("lecture_id") int lecture_id, @RequestBody String login) {
        int serviceResponse = userLectureService.cancelReservation(login, lecture_id);
        if(serviceResponse == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (serviceResponse == 1) {
            return new ResponseEntity<>("Podany użytkownik nie zarezerwował miejsca na podanej prelekcji", HttpStatus.FORBIDDEN);
        } else if (serviceResponse == 2) {
            return new ResponseEntity<>("Podana prelekcja nie istnieje", HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Podany użytkownik nie istnieje", HttpStatus.FORBIDDEN);
        }
    }
}
