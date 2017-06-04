package com.awesomegroup.fridge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Michał on 2017-05-28.
 */
@Controller
@RequestMapping("/partials/fridge")
public class FridgePartialsController {

    @GetMapping("/fridge")
    public String displayFridgeDefaultView() {
        return "partials/fridge/fridge";
    }
}
