package com.shj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiaoyaolan on 2017/4/18.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }
}
