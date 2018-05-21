package com.basm.slots.controller;

import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.model.SlotResult;
import com.basm.slots.restmodel.PlayerWalletInfo;
import com.basm.slots.service.PlayerWalletService;
import com.basm.slots.service.SlotService;
import com.basm.slots.util.OutgoingTxToPlayerWalletInfoResultConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "http://localhost" }, maxAge = 3000)
public class SlotsController {

    private final Logger log = LoggerFactory.getLogger(SlotsController.class);

    @Autowired
    private PlayerWalletService playerWalletService;

    @Autowired
    private SlotService slotsService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/loginPublicKey", method = RequestMethod.POST)
    public PlayerWalletInfo loginPublicKey(@RequestParam(value="publicKey") final String publicKey) {
        log.info("Logging in " + publicKey);
        PlayerWalletInfo playerWalletInfo = new PlayerWalletInfo(publicKey, playerWalletService.getFundsForPublicKey(publicKey));
        List<OutgoingPlayerWalletTransaction> outgoingPlayerWalletTransactionList = playerWalletService.findLast10OutgoingTransactionsForPublicKey(publicKey);
        OutgoingTxToPlayerWalletInfoResultConverter converter = new OutgoingTxToPlayerWalletInfoResultConverter();
        for(OutgoingPlayerWalletTransaction tx : outgoingPlayerWalletTransactionList) {
            playerWalletInfo.addResult(converter.convert(tx));
        }
        return playerWalletInfo;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/playslots", method =  RequestMethod.POST)
    public SlotResult playSlots(@RequestParam(value="publicKey") final String publicKey) throws Exception {
        log.info("Spinning slot for " + publicKey);
        SlotResult result = slotsService.playSlots(publicKey);
        log.info(publicKey + " won " + result.getAmount());
        return result;
    }
}
