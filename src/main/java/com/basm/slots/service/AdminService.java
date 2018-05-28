package com.basm.slots.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.StatefulConfiguration;
import com.basm.slots.repository.StatefulConfigurationRepository;
import com.basm.slots.restmodel.AdminStats;
import com.basm.slots.restmodel.PlayerWalletInfo;

@Service("AdminService")
public class AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private StatefulConfigurationRepository statefulConfigurationRepository;

    @Autowired
    private SlotsProperties slotsProperties;

    @Autowired
    private StellarService stellarService;
    
    @Autowired
    private PlayerWalletService playerWalletService;
    
    private boolean isPrivateKeyTheGamesPrivateKey(final String privateKey) {
        return slotsProperties.getEscrowWalletPrivateKey().equals(privateKey);
    }

    public String setupBigPayoutForNextSpin(final String privateKey) {
        if(isPrivateKeyTheGamesPrivateKey(privateKey)) {
            StatefulConfiguration config = statefulConfigurationRepository.findByName("production");
            config.setBigPayout(true);
            statefulConfigurationRepository.save(config);
            String message = "The next spin will be a big win.";
            log.info(message);
            return message;
        } else {
            log.warn("Potential threat detected, invalid private key for trigger big win");
            throw new RuntimeException("Invalid private key");
        }
    }
    
    public AdminStats getAdminStats(final String privateKey) {
    	if(isPrivateKeyTheGamesPrivateKey(privateKey)) {
    		AdminStats adminStats = new AdminStats();
        	try {
    			adminStats.setAmountInWallet(stellarService.getAvailableAmountInSlotsWallet());
    			List<PlayerWallet> activeWallets = playerWalletService.findPlayerWalletsWithBalanceGreaterThen0();
    			for(PlayerWallet pw : activeWallets) { 
    				adminStats.addPlayerWalletInfo(new PlayerWalletInfo(pw.getPublicKey(), pw.getBalance()));
    			}
    		} catch (IOException e) {
    			log.error("Error accessing the horizon servers");
    		}
        	return adminStats;
    	} else {
    		log.warn("Potential threat detected, invalid private key for trigger big win");
            throw new RuntimeException("Invalid private key");
    	}
    }

}
