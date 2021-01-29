package ua.antonfedoruk.sweater.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.antonfedoruk.sweater.model.Role;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.repository.UserRepository;

import java.util.Collections;

//      Example of module test
//      This type of tests usually used to check logic functions that don't have outer dependencies
// for example we don't need to create MailSender an DB to test UserService, as we want to test only logic of user adding
//      Exactly the Mockito.mock() method allows us to create a mock object of a class or an interface.
// Then, we can use the mock to stub return values for its methods and verify if they were called.
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {
    //https://code.google.com/archive/p/hamcrest/wikis/Tutorial.wiki
    //it these tests: hamcrest matchers are commonly in use, to create complicate tests

    //    Mockito's @Mock Annotation
    // This annotation is a shorthand for the Mockito.mock() method. As well, we should only use it in a test class. Unlike the mock() method, we need to enable Mockito annotations to use this annotation.
    // We can do this either by using the MockitoJUnitRunner to run the test or calling the MockitoAnnotations.initMocks() method explicitly.
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    /* in this test we check that user will obtain:
    - active: true
    - Role: USER
    - activationCode
    - password;
    thу attempt:
    - to save user
    - to send confirmation mail
    */
    @Test
    public void addUserTest() {
        User user = new User();

        user.setEmail("some@mail.com");
        boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER))); //CoreMatcher from Hamkrest

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    // test the situation of user's creation fail, when this user is already exists an message sending
    @Test
    public void addUserFailTest() {
        User user = new User();

        //DB emulation with our needs
        Mockito.doReturn(new User())        // get new user
                .when(userRepository)       // when userRepo's
                .findByUsername("user1");   // method findByUsername() is calling with such param 'user1'

        user.setUsername("user1");
        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );

    }

    @Test
    public void activateUserTest() {
        User user = new User();

        user.setActivationCode("foo");

        Mockito.doReturn(user)
                .when(userRepository)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertFalse(isUserActivated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}