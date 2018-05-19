package com.basm.slots.controller;

import com.basm.slots.model.SlotResult;
import com.basm.slots.restmodel.PlayerWalletInfo;
import com.basm.slots.service.PlayerWalletService;
import com.basm.slots.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = { "http://localhost" }, maxAge = 3000)
public class SlotsController {

    @Autowired
    private PlayerWalletService playerWalletService;

    @Autowired
    private SlotService slotsService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/fundsinwalletfor", method = RequestMethod.POST)
    public PlayerWalletInfo getFundsInWalletFor(@RequestParam(value="publicKey") final String publicKey) {
        PlayerWalletInfo playerWalletInfo = new PlayerWalletInfo(publicKey, playerWalletService.getFundsForPublicKey(publicKey));
        return playerWalletInfo;
    }

    @RequestMapping(value = "/playslots", method =  RequestMethod.POST)
    public SlotResult playSlots(@RequestParam(value="publicKey") final String publicKey) throws Exception {
        return slotsService.playSlots(publicKey);
    }
}
