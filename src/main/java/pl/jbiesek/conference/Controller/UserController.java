package pl.jbiesek.conference.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jbiesek.conference.Dto.UpdateEmailDto;
import pl.jbiesek.conference.Entites.User;
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

    @PostMapping("/user")
    public ResponseEntity<String> add(@RequestBody User user) {
        int userServiceResponse = userService.add(user);
        if (userServiceResponse==0) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else if (userServiceResponse == 1) {
            return new ResponseEntity<>("Podano nieprawidłowy adres e-mail",HttpStatus.FORBIDDEN);
        } else if (userServiceResponse == 2) {
            return new ResponseEntity<>("Podany login jest już zajęty.", HttpStatus.FORBIDDEN);
        } else if (userServiceResponse == 3) {
            return new ResponseEntity<>("Podany e-mail jest już zajęty.", HttpStatus.FORBIDDEN);
        } else if (userServiceResponse == 4) {
            return new ResponseEntity<>("W bazie istnieje użytkownik o podanym loginie i e-mailu.", HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Wpisano nieprawidłowe dane.", HttpStatus.FORBIDDEN);
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
        int serviceResponse = userService.updateEmail(updateEmailDto);
        if(serviceResponse == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (serviceResponse == 1) {
            return new ResponseEntity<>("Użytkownik o podanym loginie nie istnieje", HttpStatus.FORBIDDEN);
        } else if (serviceResponse == 2) {
            return new ResponseEntity<>("Użytkownik o podanym e-mailu nie istnieje", HttpStatus.FORBIDDEN);
        } else if (serviceResponse == 3) {
            return new ResponseEntity<>("Użytkownik o podanym loginie i e-mailu nie istnieje", HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Podano nieprawidłowy adres e-mail", HttpStatus.FORBIDDEN);
        }
    }
}
