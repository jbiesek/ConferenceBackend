package pl.jbiesek.conference.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Responses.MessageResponse;
import pl.jbiesek.conference.Services.LectureService;
import pl.jbiesek.conference.Services.UserLectureService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Lecture")
public class LectureController {

    @Autowired
    LectureService lectureService;

    @Autowired
    UserLectureService userLectureService;

    @GetMapping("/lectures/sortByDate")
    public List<Lecture> getAllByDate() {
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
    public ResponseEntity<Lecture> getById(@PathVariable("id") int id) {
        Optional<Lecture> lecture = lectureService.getById(id);
        return lecture.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/lecture/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        if (lectureService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/lecture/signIn/{lecture_id}")
    public ResponseEntity<String> signIn (@PathVariable("lecture_id") int lecture_id, @RequestBody User user) throws IOException {
        MessageResponse serviceResponse = userLectureService.signUserIntoLecture(lecture_id, user.getLogin(), user.getEmail());
        if(serviceResponse.getSuccess()) {
            return new ResponseEntity<>(serviceResponse.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(serviceResponse.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/lecture/cancelReservation/{lecture_id}")
    public ResponseEntity<String> cancelReservation(@PathVariable("lecture_id") int lecture_id, @RequestBody String login) {
        MessageResponse serviceResponse = userLectureService.cancelReservation(login, lecture_id);
        if(serviceResponse.getSuccess()) {
            return new ResponseEntity<>(serviceResponse.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(serviceResponse.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/lectures/generateLectureReport")
    public ResponseEntity<String> generateLectureReport() throws IOException {
        return new ResponseEntity<>(userLectureService.generateLectureReport(), HttpStatus.OK);
    }

    @PostMapping("/lectures/generateThemeReport")
    public ResponseEntity<String> generateThemeReport() throws IOException {
        return new ResponseEntity<>(userLectureService.generateLectureReportByTheme(), HttpStatus.OK);
    }
}
