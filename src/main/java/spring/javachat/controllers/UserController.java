package spring.javachat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.javachat.models.entity.Role;
import spring.javachat.models.entity.User;
import spring.javachat.models.service.CustomUserDetails;
import spring.javachat.models.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "signup";
        if (userService.findUserByLogin(user.getLogin()) != null) {
            model.addAttribute("userExists", "Такой пользователь уже зарегистрирован!");
            return "signup";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userService.save(user);
        return "signup_success";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(Model model,
                          @RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "logout", required = false) String logout) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @GetMapping("/chat")
    public String showChat(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((CustomUserDetails) principal).getUsername();
        model.addAttribute("name", username);
        return "chat";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/admin/users")
    public String viewUsersList(Model model){
        List<User> users  = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/edit/{Id}")
    public String editUser(@PathVariable("Id") Integer Id, Model model){
        User user = userService.get(Id);
        List<Role> listRoles = userService.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "editUserRoles";
    }

    @PostMapping("/users/save")
    public String saveUser(User user){
        userService.saveEdit(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/fun")
    public String fun() {
        return "fun";
    }
}
