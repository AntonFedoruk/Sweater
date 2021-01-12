package ua.antonfedoruk.sweater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.antonfedoruk.sweater.model.Message;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.repository.MessageRepository;

import java.util.Map;

@Controller
public class MainController {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/messages")
    public String main(@RequestParam(required = false) String filter, Model model) {
        Iterable<Message> messages = messageRepository.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/messages")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model,
            @RequestParam("file")MultipartFile file) {
        Message message = new Message(text, tag, user);

        if (file != null) {

        }

        messageRepository.save(message);

        model.put("messages", messageRepository.findAll());
        return "main";
    }

//    @DeleteMapping("/messages/{tag}")
//    public String deleteByTag(@PathVariable String tag) {
//        messageRepository.deleteByTag(tag);
//        return "redirect:/messages";
//    }
}