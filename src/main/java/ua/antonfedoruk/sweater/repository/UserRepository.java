package ua.antonfedoruk.sweater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.antonfedoruk.sweater.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
