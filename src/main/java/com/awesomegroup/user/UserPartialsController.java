package com.awesomegroup.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Michał on 2017-04-23.
 */
@Controller
@RequestMapping("/partials/user")
public class UserPartialsController {

    @GetMapping("/confirm")
    public String displayConfirmPartial() {
        return "partials/user/confirm";
    }

    @GetMapping("/register")
    public String displayRegisterPartial() {
        return "partials/user/register";
    }

    @GetMapping("/login")
    public String displayLoginPartial() {
        return "partials/user/login";
    }

}
