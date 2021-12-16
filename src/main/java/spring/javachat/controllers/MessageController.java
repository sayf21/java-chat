package spring.javachat.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.javachat.models.entity.Message;
import spring.javachat.models.entity.User;
import spring.javachat.models.service.MessageService;
import spring.javachat.models.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


@Controller
public class MessageController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss", Locale.US);

    private final MessageService messageService;

    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @MessageMapping("/sendMessageToChat")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload String jsonMessage) {
        //Отправляем сообщение всем пользователям в сессии
        JSONObject jsonObject = new JSONObject(jsonMessage);
        Message message = new Message();
        message.setUser(userService.findUserByLogin(jsonObject.getString("sender")));
        message.setText(jsonObject.getString("text"));
        message.setSendingTime(LocalDateTime.parse(jsonObject.getString("sendingTime"), formatter));
        message.setType(Message.MessageType.CHAT);
        messageService.createMessage(message);
        return message;
    }

    @MessageMapping("/addUserToChat")
    @SendTo("/topic/public")
    public Message addUser(@Payload String json, SimpMessageHeaderAccessor headerAccessor) {
        // Добавляем пользователя в сессию WS
        JSONObject jsonObject = new JSONObject(json);
        Message message = new Message();
        message.setUser(userService.findUserByLogin(jsonObject.getString("username")));
        message.setType(Message.MessageType.JOIN);
        message.setText(message.getUser().getLogin() + " вошел(-ла) в чат!");
        message.setSendingTime(LocalDateTime.parse(jsonObject.getString("sendingTime"), formatter));
        messageService.createMessage(message);
        headerAccessor.getSessionAttributes().put("user", message.getUser());
        return message;
    }

    @GetMapping("/admin/messages")
    public String viewUsersList(Model model){
        List<Message> messages  = messageService.findAll();
        model.addAttribute("messages", messages);
        return "messages";
    }
}
