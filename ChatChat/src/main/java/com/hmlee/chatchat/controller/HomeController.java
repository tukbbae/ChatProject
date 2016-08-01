package com.hmlee.chatchat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hmlee.chatchat.core.base.BaseController;

/**
 * Created by hmlee
 */
@Controller
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/")
    public String index(ModelMap model) throws Exception {
        return "views/index2";
    }

}
