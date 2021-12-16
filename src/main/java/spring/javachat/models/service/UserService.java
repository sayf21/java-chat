package spring.javachat.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.javachat.models.entity.Role;
import spring.javachat.models.repository.RoleRepository;
import spring.javachat.models.repository.UserRepository;
import spring.javachat.models.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void save(User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getById(1));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }

    public User findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public User get(Integer Id) {
        return userRepository.findUserById(Id);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void saveEdit(User user) {

        userRepository.save(user);
    }
}
