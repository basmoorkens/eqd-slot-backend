package com.basm.slots.controller;

import com.basm.slots.model.OutgoingPlayerWalletStellarTransaction;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.SlotWinning;
import com.basm.slots.restmodel.Payout;
import com.basm.slots.restmodel.PlayerWalletInfo;
import com.basm.slots.restmodel.PlayerWalletInfoWinning;
import com.basm.slots.restmodel.SpinResultInfo;
import com.basm.slots.service.PlayerWalletService;
import com.basm.slots.service.SlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
        PlayerWallet playerWallet = playerWalletService.findPlayerWalletByPublicKey(publicKey);
        PlayerWalletInfo playerWalletInfo = new PlayerWalletInfo(publicKey, playerWallet.getBalance());
        buildLastWinningsInfo(playerWallet, playerWalletInfo);

        return playerWalletInfo;
    }

    private void buildLastWinningsInfo(PlayerWallet playerWallet, PlayerWalletInfo playerWalletInfo) {
        List<SlotWinning> last10Winnings = playerWalletService.findLast10WinningsForPlayerWallet(playerWallet);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SlotWinning winning : last10Winnings) {
            PlayerWalletInfoWinning pWinning = new PlayerWalletInfoWinning(sdf.format(winning.getCreatedDate()), winning.getAmount());
            playerWalletInfo.addResult(pWinning);
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/playslots", method =  RequestMethod.POST)
    public SpinResultInfo playSlots(@RequestParam(value="publicKey") final String publicKey) throws Exception {
        log.info("Spinning slot for " + publicKey);
        SlotWinning winning = slotsService.playSlots(publicKey);
        log.info(publicKey + " won " + winning.getAmount());
        SpinResultInfo resultInfo = new SpinResultInfo(winning);
        return resultInfo;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/payout", method =  RequestMethod.POST)
    public Payout payout(@RequestParam(value="publicKey") final String publicKey, final double amount) throws Exception {
        log.info("Payout request for " + publicKey + " with amount " + amount);
        OutgoingPlayerWalletStellarTransaction tx = playerWalletService.createPayout(publicKey, amount);
        return new Payout(tx.getPublicKey(), tx.getAmount());
    }
}
