package pl.jbiesek.conference.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jbiesek.conference.entites.User;
import pl.jbiesek.conference.request.UpdateEmailRequest;
import pl.jbiesek.conference.responses.MessageResponse;
import pl.jbiesek.conference.respositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImplement(userRepository);
    }


    @Test
    void getAll() {
        //when
        userService.getAll();
        //then
        verify(userRepository).findAll();
    }

    @Test
    void ShouldGetUserById() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));

        //when
        Optional<User> userFromService = userService.getById(1);

        //then
        assertTrue(userFromService.isPresent());
        assertThat(userFromService.get()).isEqualTo(user);
    }

    @Test
    void ShouldNotGetUserById() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        Optional<User> userFromService = userService.getById(1);

        //then
        assertFalse(userFromService.isPresent());
    }

    @Test
    void shouldAddNewStudent() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);

        //when
        userService.add(user);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void shouldNotAddUserWithTakenLoginAndTakenEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.getUserByLoginAndEmail(anyString(),anyString())).willReturn(Optional.of(user));

        //then
        assertFalse(userService.add(user).getSuccess());
    }

    @Test
    void shouldNotAddUserWithTakenEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.getUserByEmail(anyString())).willReturn(Optional.of(user));

        //then
        assertFalse(userService.add(user).getSuccess());
    }

    @Test
    void shouldNotAddUserWithTakenLogin() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.getUserByLogin(anyString())).willReturn(Optional.of(user));

        //then
        assertFalse(userService.add(user).getSuccess());
    }

    @Test
    void shouldNotAddUserWithInvalidEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski";
        User user = new User(login, email);

        //then
        assertFalse(userService.add(user).getSuccess());
    }

    @Test
    void shouldUpdateUser() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        String updatedLogin = "Jan1";
        String upadtedEmail = "jan_kowalski1@gmail.com";
        User updatedUser = new User(updatedLogin, upadtedEmail);
        given(userRepository.existsById(anyInt())).willReturn(true);
        given(userRepository.getReferenceById(anyInt())).willReturn(user);

        //then
        assertTrue(userService.update(1, updatedUser));
    }

    @Test
    void shouldNotUpdateUser() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        String updatedLogin = "Jan1";
        String upadtedEmail = "jan_kowalski1@gmail.com";
        User updatedUser = new User(updatedLogin, upadtedEmail);
        given(userRepository.existsById(anyInt())).willReturn(false);

        //then
        assertFalse(userService.update(1, updatedUser));
    }

    @Test
    void shouldDeleteUser() {
        //given
        given(userRepository.existsById(anyInt())).willReturn(true);

        //then
        assertTrue(userService.delete(1));
    }

    @Test
    void shouldNotDeleteUser() {
        //given
        given(userRepository.existsById(anyInt())).willReturn(false);

        //then
        assertFalse(userService.delete(1));
    }

    @Test
    void ShouldUpdateEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.getUserByLogin(anyString())).willReturn(Optional.of(user));
        given(userRepository.getUserByEmail(anyString())).willReturn(Optional.of(user));
        given(userRepository.getUserByLoginAndEmail(anyString(),anyString())).willReturn(Optional.of(user));
        given(userRepository.checkForEmail(anyString())).willReturn(0);
        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest("Jan", "jan_kowalski@gmail.com", "jan_kowalski1@gmail.com");

        //then
        assertTrue(userService.updateEmail(updateEmailRequest).getSuccess());
    }

    @Test
    void ShouldNotUpdateEmailUserNotExists() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        given(userRepository.getUserByLogin(anyString())).willReturn(Optional.empty());
        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest("Jan", "jan_kowalski@gmail.com", "jan_kowalski1@gmail.com");

        //then
        assertFalse(userService.updateEmail(updateEmailRequest).getSuccess());
    }

}