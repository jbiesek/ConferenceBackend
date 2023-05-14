package pl.jbiesek.conference.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jbiesek.conference.Dtos.UpdateEmailDto;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Responses.MessageResponse;
import pl.jbiesek.conference.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/user/{id}")
    public User getById(@PathVariable("id") int id) {
        return userService.getById(id);
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> add(@RequestBody User user) {
        MessageResponse userServiceResponse = userService.add(user);
        if (userServiceResponse.getSuccess()) {
            return new ResponseEntity<>(userServiceResponse.getMessage(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(userServiceResponse.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody User updatedUser) {
        if(userService.update(id, updatedUser)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        if(userService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/user/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestBody UpdateEmailDto updateEmailDto) {
        MessageResponse serviceResponse = userService.updateEmail(updateEmailDto);
        if(serviceResponse.getSuccess()) {
            return new ResponseEntity<>(serviceResponse.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(serviceResponse.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
