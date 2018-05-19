package com.basm.slots.controller;

import com.basm.slots.service.StellarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private StellarService stellarService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello() throws IOException {
        stellarService.printWallets();
        return "hello";
    }
}
