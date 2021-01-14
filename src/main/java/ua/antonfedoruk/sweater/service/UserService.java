package ua.antonfedoruk.sweater.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.antonfedoruk.sweater.model.Role;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service("userService")
public class UserService implements UserDetailsService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    //return true if adding completed successfully
    public boolean addUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        //if we found users account in DB we should return false
        if (userFromDB != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER)); //Collections.singleton() creates Set with 1 value
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \r" +
                    "Welcome to Sweater. Please visit next link: " +
                    "http://localhost:8080/registration/activate/%s", user.getUsername(), user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null); //it's mean that user confirmed email
        user.setActive(true);

        userRepository.save(user);

        return true;
    }
}
