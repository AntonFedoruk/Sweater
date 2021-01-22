package ua.antonfedoruk.sweater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.antonfedoruk.sweater.model.Role;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") //verify user authority
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @GetMapping("/{user}")
    @PreAuthorize("hasAuthority('ADMIN')") //verify user authority
    public String userEditForm(@PathVariable User user, Model model) { //Spring is clever and can get User without using repository
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')") //verify user authority
    public String userSave(@RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam("userId") User user, Model model) {
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email) {
        userService.updateProfile(user, password, email);
        return "redirect:/user/profile";
    }

    @GetMapping("/unsubscribe/{userChannelId}")
    public String unsubscribe(@PathVariable(name = "userChannelId") User user,
                              @AuthenticationPrincipal User currentUser) {
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("/subscribe/{userChannelId}")
    public String subscribe(@PathVariable(name = "userChannelId") User user,
                              @AuthenticationPrincipal User currentUser) {
        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("{type}/{userChannelId}/list")
    public String userList(@PathVariable(name = "userChannelId") User user,
                           @PathVariable String type,
                           Model model) {
        model.addAttribute("type", type);
        model.addAttribute("userChannel", user);

        if (type.equals("subscriptions")) {
            model.addAttribute("users", user.getSubscriptions());
        } else if (type.equals("subscribers")){
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }
}
