package com.zzy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sheep on 2019/11/12.
 */
@Controller
public class IndexController {

    private static final String INDEX_HTML = "index";


    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return INDEX_HTML;
    }

    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String indexHtml() {
        return index();
    }
}
