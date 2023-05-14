package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Dtos.UpdateEmailDto;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Responses.MessageResponse;

import java.util.List;

public interface UserService {

    public List<User> getAll();

    public User getById(int id);

    public MessageResponse add(User user);

    public Boolean update(int id, User updatedUser);

    public Boolean delete(int id);

    public MessageResponse updateEmail(UpdateEmailDto updateEmailDto);
}
