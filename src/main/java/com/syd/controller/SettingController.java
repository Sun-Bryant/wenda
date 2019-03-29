package com.syd.controller;

import com.syd.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SettingController {

    @Autowired
    WendaService wendaService;//享元模式

    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting() {
        return "Setting OK" + wendaService.getMessage(1);
    }

}
