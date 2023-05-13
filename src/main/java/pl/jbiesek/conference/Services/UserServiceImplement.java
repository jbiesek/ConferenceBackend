package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Dto.UpdateEmailDto;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Respositories.UserRepository;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(int id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public int add(User user) {
        if (user.getEmail()==null || user.getLogin()==null) {
            return 5;
        }
        if (userRepository.getUserByLoginAndEmail(user.getLogin(), user.getEmail()).isPresent()) {
            return 4;
        }
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            return 3;
        }
        if (userRepository.getUserByLogin(user.getLogin()).isPresent()) {
            return 2;
        }
        if (!emailCheck(user.getEmail())) {
            return 1;
        }
        userRepository.save(user);
        return 0;
    }

    @Override
    public Boolean update(int id, User updatedUser) {
        if(!userRepository.existsById(id)){
            return false;
        }
        User user = userRepository.getReferenceById(id);
        if(updatedUser.getEmail()!=null){
            user.setEmail(updatedUser.getEmail());
        }
        if(updatedUser.getLogin()!=null){
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
    public int updateEmail(UpdateEmailDto updateEmailDto) {
        String login = updateEmailDto.getLogin();
        String email = updateEmailDto.getEmail();
        String updatedEmail = updateEmailDto.getUpdatedEmail();
        if (userRepository.getUserByLogin(login).isEmpty()) {
            return 1;
        }
        if (userRepository.getUserByEmail(email).isEmpty()) {
            return 2;
        }
        if (userRepository.getUserByLoginAndEmail(login, email).isEmpty()) {
            return 3;
        }
        User userWithId = userRepository.getUserByLoginAndEmail(login, email).get();
        if (!emailCheck(updatedEmail)) {
            return 4;
        }
        if (email.equals(updatedEmail)){
            return 5;
        }
        userWithId.setEmail(updatedEmail);
        userRepository.save(userWithId);
        return 0;
    }

    private Boolean emailCheck(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(pattern);
        return p.matcher(email).matches();
    }
}
