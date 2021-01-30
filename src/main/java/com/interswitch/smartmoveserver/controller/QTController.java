package com.interswitch.smartmoveserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Created by adebola.owolabi on 8/11/2020
 */
@Controller
@RequestMapping("/qt-transport")
public class QTController {
    @GetMapping(value = {"/", "/index"})
    public String qthome(Model model) {
        return "qt-transport/index";
    }

    @GetMapping(value = {"/bus-list"})
    public String bs(Model model) {
        return "qt-transport/bus-list";
    }

    @PostMapping(value = {"/bus-list"})
    public String bsp(Model model) {
        return "qt-transport/bus-list";
    }

    @GetMapping(value = {"/confirm-booking"})
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
