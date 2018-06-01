package com.basm.slots.controller;

import com.basm.slots.restmodel.AdminStats;
import com.basm.slots.service.AdminService;
import com.basm.slots.service.PlayerWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private PlayerWalletService playerWalletService;

    @Autowired
    private AdminService adminService;


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/triggerBigPayout", method =  RequestMethod.GET, produces = "application/json")
    public String triggerBigPayout(@RequestParam(value="privateKey") final String privateKey) throws Exception {
        log.info("Trigger big payout called");
        if(privateKey == null) {
            throw new RuntimeException("You need to provide the game wallets private key to access this functionality.");
        }
        return adminService.setupBigPayoutForNextSpin(privateKey);
    }
    
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/getAdminStats", method =  RequestMethod.POST,  produces = "application/json")
    public AdminStats getAdminStats(@RequestParam(value="privateKey") final String privateKey) throws Exception {
        log.info("Fetching admin stats");
        if(privateKey == null) {
            throw new RuntimeException("You need to provide the game wallets private key to access this functionality.");
        }
        return adminService.getAdminStats(privateKey);
    }    
}
