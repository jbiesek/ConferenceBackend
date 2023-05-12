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

    @GetMapping("/lectures")
    public List<Lecture> getAll() {
        List<Lecture> lectures = lectureService.getAll();
        lectures.sort(new Comparator<Lecture>() {
            @Override
            public int compare(Lecture o1, Lecture o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return lectures;
    }

    @GetMapping("/lectures/sortByTheme")
    public List<Lecture> getAllByTheme() {
        List<Lecture> lectures = lectureService.getAll();
        lectures.sort(new Comparator<Lecture>() {
            @Override
            public int compare(Lecture o1, Lecture o2) {
                return o1.getTheme().compareTo(o2.getTheme());
            }
        });
        return lectures;
    }

    @GetMapping("/lecture/{id}")
    public Lecture getById(@PathVariable("id") int id) {
        return lectureService.getById(id);
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
    public ResponseEntity<Void> signIn (@PathVariable("lecture_id") int lecture_id, @RequestBody User user) {
        if(userLectureService.signUserIntoLecture(lecture_id, user.getLogin(), user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/lectures/byLogin")
    public List<Lecture> getLecturesByLogin(@RequestBody String login) {
        return userLectureService.getLecturesByLogin(login);
    }
}
