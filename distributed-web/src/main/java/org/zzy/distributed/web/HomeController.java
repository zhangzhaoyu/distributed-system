package org.zzy.distributed.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zhaoyu on 16-8-23.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String home() {
        return "index";
    }
}
