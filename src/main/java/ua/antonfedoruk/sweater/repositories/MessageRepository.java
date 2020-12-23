package ua.antonfedoruk.sweater.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.antonfedoruk.sweater.model.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message> findByTag(String tag);
}
