package pl.jbiesek.conference.Services;

import pl.jbiesek.conference.Request.UpdateEmailRequest;
import pl.jbiesek.conference.Entites.User;
import pl.jbiesek.conference.Responses.MessageResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getAll();

    public Optional<User> getById(int id);

    public MessageResponse add(User user);

    public Boolean update(int id, User updatedUser);

    public Boolean delete(int id);

    public MessageResponse updateEmail(UpdateEmailRequest updateEmailRequest);
}
