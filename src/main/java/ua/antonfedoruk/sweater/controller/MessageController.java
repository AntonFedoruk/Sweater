package ua.antonfedoruk.sweater.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ua.antonfedoruk.sweater.model.Message;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.model.dto.MessageDto;
import ua.antonfedoruk.sweater.repository.MessageRepository;
import ua.antonfedoruk.sweater.service.MessageService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;
    private final MessageService messageService;


    public MessageController(MessageRepository messageRepository, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/messages")
    public String main(@RequestParam(required = false) String filter,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,//Pageable serve for page-by-page display of long lists(is also added to MessageRepository);
                       // sort param show type of sorting(in our case: sorting by "id"); direction param describe direction of sort(in our case: first of all we will display latest messages added
                       // @PageableDefault needed to prevent mistakes
                       @AuthenticationPrincipal User user) //we get the current user as a parameter from session
                       {
        Page<MessageDto> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/messages");
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
            @RequestParam("file") MultipartFile file,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("errorsMap", errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);

            model.addAttribute("messages", null); //this help to close opened form after we added new message

            messageRepository.save(message);
        }
        Page<MessageDto> page = messageRepository.findAll(pageable, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/messages");
        model.addAttribute("messages", messageRepository.findAll());
        return "main";
    }

    private void saveFile(@Valid Message message,
                          @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            //create unique file name with help of UUID(Universally Unique Identifier)
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{userId}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable(name = "userId") User author,
                               Model model,
                               @RequestParam(required = false) Message message,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MessageDto> page = messageService.messageListForUser(pageable, author, currentUser);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("userChannel", author);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("page", page);
        model.addAttribute("url", "/user-messages/" + author.getId());
        return "userMessages";
    }

    @PostMapping("/user-messages/{userId}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long userId,
                                @RequestParam("id") Message message,
                                @RequestParam("text") String text,
                                @RequestParam("tag") String tag,
                                @RequestParam("file") MultipartFile file) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setText(tag);
            }
            saveFile(message, file);
            messageRepository.save(message);
        }
        return "redirect:/user-messages/" + currentUser.getId();
    }

    @GetMapping("/messages/{messageId}/like")
    public String like(@AuthenticationPrincipal User currentUser,
                       @PathVariable(name = "messageId") Message message,
                       RedirectAttributes redirectAttributes, //RedirectAttributes allows to throw attributes to redirected method
                       @RequestHeader(required = false) String referer //@RequestHeader gives to us a page where we have come from; we will use it in redirection
                       ) {
        Set<User> likes = message.getLikes();
        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        //obtain attributes from RequestHeader
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(referer).build();
        //add obtained attr to RedirectAttributes
        uriComponents.getQueryParams()
                .entrySet()
                .forEach((pair) -> redirectAttributes.addAttribute(pair.getKey(),pair.getValue()));
        //thus we  transfer all obtained attributes(pagination param, current location on page and etc. ) as parameter to redirected method

        return "redirect:" + uriComponents.getPath();
    }

//    @DeleteMapping("/messages/{tag}")
//    public String deleteByTag(@PathVariable String tag) {
//        messageRepository.deleteByTag(tag);
//        return "redirect:/messages";
//    }
}