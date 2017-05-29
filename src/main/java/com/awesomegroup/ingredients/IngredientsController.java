package com.awesomegroup.ingredients;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.Contended;

/**
 * Created by wrobe on 28.05.2017.
 */

@Controller
@RequestMapping("/partials/ingredients")
public class IngredientsController {

    @RequestMapping("main")
    public String displayIngredients() {
        return "partials/ingredients/mainIngredients";
    }

    @GetMapping("/where")
    public String Where() {
        return new String("In ingredients!");
    }

}
