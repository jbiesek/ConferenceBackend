package pl.jbiesek.conference.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Respositories.UserRepository;

import java.util.List;

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
        if (user.getEmail()!=null && user.getLogin()!=null) {
            if (userRepository.getUserByLogin(user.getLogin()).isPresent()) {
                if (userRepository.getUserByLoginAndEmail(user.getLogin(), user.getEmail()).isPresent()) {
                    return 2;
                } else {
                    return 3;
                }
            } else {
                userRepository.save(user);
                return 0;
            }
        } else {
            return 1;
        }
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
}
