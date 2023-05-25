package pl.jbiesek.conference.services;

import pl.jbiesek.conference.request.UpdateEmailRequest;
import pl.jbiesek.conference.entites.User;
import pl.jbiesek.conference.responses.MessageResponse;

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
