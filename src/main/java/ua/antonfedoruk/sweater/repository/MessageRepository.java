package ua.antonfedoruk.sweater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.antonfedoruk.sweater.model.Message;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.model.dto.MessageDto;

public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query("select new ua.antonfedoruk.sweater.model.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   (sum(case when ml = :user then 1 else 0 end) > 0)" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);//{Page + Pageable} used to provide pagination to our pages

    @Query("select new ua.antonfedoruk.sweater.model.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   (sum(case when ml = :user then 1 else 0 end) > 0)" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    void deleteByTag(String tag);

    @Query("select new ua.antonfedoruk.sweater.model.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   (sum(case when ml = :user then 1 else 0 end) > 0)" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDto> findByAuthor(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
