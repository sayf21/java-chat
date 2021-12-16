package spring.javachat.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import spring.javachat.models.entity.Role;

import java.util.Set;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
