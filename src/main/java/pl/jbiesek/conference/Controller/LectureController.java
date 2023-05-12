package pl.jbiesek.conference.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jbiesek.conference.Entites.Lecture;
import pl.jbiesek.conference.Services.LectureService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LectureController {

    @Autowired
    LectureService lectureService;

    @GetMapping("/lectures")
    public List<Lecture> getAll() {
        return lectureService.getAll();
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
}
