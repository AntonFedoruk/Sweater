package ua.antonfedoruk.sweater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.model.dto.CaptchaResponseDto;
import ua.antonfedoruk.sweater.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final static String  CAPTRCHA_URL= "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String getRegistrationPage() {
        return "registration";
    }

    @Value("${recaptcha.secret}")
    private String secret;



    /*
    reCAPTCHA API Request
    *********************************************************************
        URL: https://www.google.com/recaptcha/api/siteverify METHOD: POST

        POST Parameter	 |   Description
        ---------------------------------------------------------------------------------------------------------------------
        secret	         |   Required. The shared key between your site and reCAPTCHA.
        response	     |   Required. The user response token provided by the reCAPTCHA client-side integration on your site.
        remoteip	     |   Optional. The user's IP address.
     */
    @PostMapping
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @RequestParam("g-recaptcha-response") String captchaResponse, //get the user’s response token; g-recaptcha-response POST parameter when the user submits the form on our site
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        String url = String.format(CAPTRCHA_URL, secret, captchaResponse);
        CaptchaResponseDto responseDto = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!responseDto.isSuccess()) {
            model.addAttribute("captchaError", "Please, fill captcha!");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty.");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registration";
        }

        if (isConfirmEmpty || bindingResult.hasErrors() || !responseDto.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);

            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User activated successfully!");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }
        return "login";
    }

}