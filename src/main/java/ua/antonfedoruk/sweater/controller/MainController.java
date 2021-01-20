package ua.antonfedoruk.sweater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.antonfedoruk.sweater.model.Message;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.repository.MessageRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

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
            @AuthenticationPrincipal User user, //we get the current user as a parameter from session
            @Valid Message message, //@Valid start validation; due to the fact that we begin validation we must add BindingResult
//          !!!THIS('BindingResult') ARGUMENT MUST STAY BEFORE 'MODEL' ARGUMENT, to prevent their representation in view!!!
            BindingResult bindingResult, //list of arguments and messages about validation`s errors
            Model model,
            @RequestParam("file")MultipartFile file) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("errorsMap", errorsMap);
            model.addAttribute("message", message);
        } else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                //create unique file name with help of UUID(Universally Unique Identifier)
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFileName));

                message.setFilename(resultFileName);
            }

            model.addAttribute("messages", null); //this help to close opened form after we added new message

            messageRepository.save(message);
        }

        model.addAttribute("messages", messageRepository.findAll());
        return "main";
    }
//    @DeleteMapping("/messages/{tag}")
//    public String deleteByTag(@PathVariable String tag) {
//        messageRepository.deleteByTag(tag);
//        return "redirect:/messages";
//    }
}