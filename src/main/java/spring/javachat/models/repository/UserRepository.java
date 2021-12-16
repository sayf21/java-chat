package spring.javachat.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import spring.javachat.models.entity.Role;
import spring.javachat.models.entity.User;

import java.util.Set;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(int id);
    User findUserByLogin(String login);

}
