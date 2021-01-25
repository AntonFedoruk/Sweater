package ua.antonfedoruk.sweater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ua.antonfedoruk.sweater.model.Message;
import ua.antonfedoruk.sweater.model.User;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Page<Message> findByTag(String tag, Pageable pageable);//{Page + Pageable} used to provide pagination to our pages

    Page<Message> findAll(Pageable pageable);

    void deleteByTag(String tag);

    Page<Message> findByAuthor(User user, Pageable pageable);
}
