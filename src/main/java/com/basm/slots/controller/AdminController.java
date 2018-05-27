package com.basm.slots.controller;

import com.basm.slots.repository.StatefulConfigurationRepository;
import com.basm.slots.restmodel.SpinResultInfo;
import com.basm.slots.service.AdminService;
import com.basm.slots.service.PlayerWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = { "http://localhost" }, maxAge = 3000)
public class AdminController {

    private final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private PlayerWalletService playerWalletService;

    @Autowired
    private AdminService adminService;


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/triggerBigPayout", method =  RequestMethod.GET)
    public String triggerBigPayout(@RequestParam(value="publicKey") final String privateKey) throws Exception {
        log.info("Trigger big payout called");
        if(privateKey == null) {
            throw new RuntimeException("You need to provide the game wallets private key to access this functionality.");
        }
        return adminService.setupBigPayoutForNextSpin(privateKey);
    }
}
