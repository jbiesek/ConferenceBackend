package pl.jbiesek.conference.services;

import org.springframework.stereotype.Service;
import pl.jbiesek.conference.request.UpdateEmailRequest;
import pl.jbiesek.conference.entites.User;
import pl.jbiesek.conference.responses.MessageResponse;
import pl.jbiesek.conference.respositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public MessageResponse add(User user) {
        if (user.getEmail().isEmpty() || user.getLogin().isEmpty()) {
            return MessageResponse.builder().message("Podano nieprawidłowe dane.").success(false).build();
        }
        if (userRepository.getUserByLoginAndEmail(user.getLogin(), user.getEmail()).isPresent()) {
            return MessageResponse.builder().message("W bazie istnieje użytkownik o podanym loginie i e-mailu.").success(false).build();
        }
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            return MessageResponse.builder().message("Podany e-mail jest już zajęty.").success(false).build();
        }
        if (userRepository.getUserByLogin(user.getLogin()).isPresent()) {
            return MessageResponse.builder().message("Podany login jest już zajęty.").success(false).build();
        }
        if (!emailCheck(user.getEmail())) {
            return MessageResponse.builder().message("Podano nieprawidłowy adres e-mail.").success(false).build();
        }
        userRepository.save(user);
        return MessageResponse.builder().message("Pomyśnie zarejestrowano.").success(true).build();
    }

    @Override
    public Boolean update(int id, User updatedUser) {
        if(!userRepository.existsById(id)){
            return false;
        }
        User user = userRepository.getReferenceById(id);
        if(!updatedUser.getEmail().isEmpty() && !updatedUser.getEmail().equals(user.getEmail()) && emailCheck(updatedUser.getEmail())){
            user.setEmail(updatedUser.getEmail());
        }
        if(!updatedUser.getLogin().isEmpty() && !updatedUser.getLogin().equals(user.getLogin())){
            user.setLogin(updatedUser.getLogin());
        }
        try {
            userRepository.save(user);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public Boolean delete(int id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public MessageResponse updateEmail(UpdateEmailRequest updateEmailRequest) {
        String login = updateEmailRequest.getLogin();
        String email = updateEmailRequest.getEmail();
        String updatedEmail = updateEmailRequest.getUpdatedEmail();
        if (userRepository.getUserByLogin(login).isEmpty()) {
            return MessageResponse.builder().message("Użytkownik o podanym loginie nie istnieje.").success(false).build();
        }
        if (userRepository.getUserByEmail(email).isEmpty()) {
            return MessageResponse.builder().message("Użytkownik o podanym e-mailu nie istnieje.").success(false).build();
        }
        if (userRepository.getUserByLoginAndEmail(login, email).isEmpty()) {
            return MessageResponse.builder().message("Użytkownik o podanym loginie i e-mailu nie istnieje.").success(false).build();
        }
        User userWithId = userRepository.getUserByLoginAndEmail(login, email).get();
        if (!emailCheck(updatedEmail)) {
            return MessageResponse.builder().message("Podano nieprawidłowy adres e-mail.").success(false).build();
        }
        if (email.equals(updatedEmail)){
            return MessageResponse.builder().message("Nowy e-mail nie może być taki sam jak stary e-mail.").success(false).build();
        }
        if(emailCheckIfExistsInDB(updatedEmail)) {
            return MessageResponse.builder().message("Podany e-mail jest już zajęty.").success(false).build();
        }
        userWithId.setEmail(updatedEmail);
        userRepository.save(userWithId);
        return MessageResponse.builder().message("Pomyślnie zmieniono adres e-mail.").success(true).build();
    }

    private Boolean emailCheck(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(pattern);
        return p.matcher(email).matches();
    }

    private Boolean emailCheckIfExistsInDB(String email) {
        return userRepository.checkForEmail(email) != 0;
    }
}
