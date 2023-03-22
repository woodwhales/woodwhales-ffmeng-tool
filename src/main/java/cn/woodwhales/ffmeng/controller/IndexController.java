package cn.woodwhales.ffmeng.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woodwhales on 2023-03-22 16:10
 */
@RequestMapping
public class IndexController {

    @GetMapping({"", "/", "/index"})
    public String index() {
        return "index";
    }

}
