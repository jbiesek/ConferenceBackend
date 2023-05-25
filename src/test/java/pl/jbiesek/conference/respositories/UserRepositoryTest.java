package pl.jbiesek.conference.respositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.jbiesek.conference.entites.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldGetUserByLoginAndEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testLogin = "Jan";
        String testEmail = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByLoginAndEmail(testLogin,testEmail);

        //then
        assertTrue(expectedUser.isPresent());
        assertEquals(expectedUser.get(),user);

    }

    @Test
    void itShouldNotGetUserByInvalidLoginAndValidEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testLogin = "Jan1";
        String testEmail = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByLoginAndEmail(testLogin,testEmail);

        //then
        assertFalse(expectedUser.isPresent());

    }

    @Test
    void itShouldNotGetUserByValidLoginAndInvalidEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testLogin = "Jan";
        String testEmail = "jan_kowalski1@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByLoginAndEmail(testLogin,testEmail);

        //then
        assertFalse(expectedUser.isPresent());

    }

    @Test
    void itShouldNotGetUserByInvalidLoginAndInvalidEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testLogin = "Jan1";
        String testEmail = "jan_kowalski1@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByLoginAndEmail(testLogin,testEmail);

        //then
        assertFalse(expectedUser.isPresent());

    }

    @Test
    void itShouldGetUserByLogin() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testLogin = "Jan";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByLogin(testLogin);

        //then
        assertTrue(expectedUser.isPresent());
        assertEquals(expectedUser.get(),user);
    }

    @Test
    void itShouldNotGetUserByLogin() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testLogin = "Jan1";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByLogin(testLogin);

        //then
        assertFalse(expectedUser.isPresent());
    }

    @Test
    void itShouldGetUserByEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testEmail = "jan_kowalski@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByEmail(testEmail);

        //then
        assertTrue(expectedUser.isPresent());
        assertEquals(expectedUser.get(),user);
    }

    @Test
    void itShouldNotGetUserByEmail() {
        //given
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testEmail = "jan_kowalski1@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.getUserByEmail(testEmail);

        //then
        assertFalse(expectedUser.isPresent());
    }

    @Test
    void itShouldCountUsersWithGivenEmail() {
        String login = "Jan";
        String email = "jan_kowalski@gmail.com";
        String testEmail_1 = "jan_kowalski@gmail.com";
        String testEmail_2 = "jan_kowalski1@gmail.com";
        User user = new User(login, email);
        userRepository.save(user);

        //when
        int count_1 = userRepository.checkForEmail(testEmail_1);
        int count_0 = userRepository.checkForEmail(testEmail_2);

        //then
        assertEquals(count_1,1);
        assertEquals(count_0, 0);
    }
}