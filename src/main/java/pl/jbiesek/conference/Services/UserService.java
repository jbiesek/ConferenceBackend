package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Entites.User;

import java.util.List;

public interface UserService {

    public List<User> getAll();

    public User getById(int id);

    public Boolean add(User user);

    public Boolean update(int id, User updatedUser);

    public Boolean delete(int id);
}
