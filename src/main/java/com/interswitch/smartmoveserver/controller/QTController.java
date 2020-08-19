package com.interswitch.smartmoveserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * Created by adebola.owolabi on 8/11/2020
 */
@Controller
public class QTController {
    @GetMapping(value = {"/qt-transport", "/qt-transport/index"})
    public String qthome(Model model) {
        return "qt-transport/index";
    }

    @GetMapping(value = {"/qt-transport/bus-list"})
    public String bs(Model model) {
        return "qt-transport/bus-list";
    }

    @PostMapping (value = {"/qt-transport/bus-list"})
    public String bsp(Model model) {
        return "qt-transport/bus-list";
    }

    @GetMapping(value = {"/qt-transport/confirm-booking"})
    public String cb(Model model) {
        return "qt-transport/confirm-booking";
    }

    @GetMapping(value = {"/qt-transport/payment"})
    public String py(Model model) {
        return "qt-transport/payment";
    }

    @GetMapping(value = {"/qt-transport/receipt"})
    public String rt(Model model) {
        return "qt-transport/receipt";
    }

    @PostMapping(value = {"/qt-transport/receipt"})
    public String rtp(Model model) {
        return "qt-transport/receipt";
    }
}
