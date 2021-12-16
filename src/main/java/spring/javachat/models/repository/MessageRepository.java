package spring.javachat.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import spring.javachat.models.entity.Message;
import spring.javachat.models.entity.User;

import java.util.List;

@Transactional
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findMessagesByUser(User user);
}
