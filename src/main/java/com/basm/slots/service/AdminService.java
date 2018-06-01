package com.basm.slots.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import com.basm.slots.restmodel.BigPayoutResponse;
import com.basm.slots.restmodel.SlotWinningStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.SlotResultFactory;
import com.basm.slots.model.SlotWinning;
import com.basm.slots.model.StatefulConfiguration;
import com.basm.slots.repository.SlotWinningRepository;
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
    private SlotWinningRepository slotWinningRepository;
    
    @Autowired
    private PlayerWalletService playerWalletService;
    
    private SlotResultFactory slotResultFactory;
    
    @PostConstruct
    public void initialize() { 
    	slotResultFactory = new SlotResultFactory();
    }
    
    private boolean isPrivateKeyTheGamesPrivateKey(final String privateKey) {
        return slotsProperties.getEscrowWalletPrivateKey().equals(privateKey);
    }

    public BigPayoutResponse setupBigPayoutForNextSpin(final String privateKey) {
        if(isPrivateKeyTheGamesPrivateKey(privateKey)) {
            StatefulConfiguration config = statefulConfigurationRepository.findByName("production");
            config.setBigPayout(true);
            statefulConfigurationRepository.save(config);
            String message = "The next spin will be a big win.";
            log.info(message);
            return new BigPayoutResponse(message);
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
    			double actualAmountInWallet = adminStats.getAmountInWallet();
    			for(PlayerWallet pw : activeWallets) { 
    				adminStats.addPlayerWalletInfo(new PlayerWalletInfo(pw.getPublicKey(), pw.getBalance()));
    				actualAmountInWallet -= pw.getBalance();
    			}
    			adminStats.setActualAmountInWallet(actualAmountInWallet);
    			adminStats.setTotalSpins(slotWinningRepository.findTotalSpins());
    			SlotWinning lastBigWin = slotWinningRepository.findLastBigwin(slotResultFactory.getResultX500().getAmount());
    			long idToStartSearch = 0l;
    			if(lastBigWin != null) {
                    lastBigWin.getId();
                }
    			List<SlotWinningStatistic> stats = slotWinningRepository.findStatsSinceLastBigSpin(idToStartSearch);
                adminStats.setStatsSinceLastBigWin(stats);
                double resultsSinceLastBigWin = 0d;
                for(SlotWinningStatistic s : stats) {
                    resultsSinceLastBigWin += slotsProperties.getAmountToSpin();
                    resultsSinceLastBigWin -= (s.getCount() * s.getPrice());
                }
                adminStats.setResultsSinceLastBigWin(resultsSinceLastBigWin);
    		} catch (IOException e) {
    			log.error("Error accessing the horizon servers");
    		}
        	return adminStats;
    	} else {
    		log.warn("Potential threat detected, invalid private key for fetching admin stats");
            throw new RuntimeException("Invalid private key");
    	}
    }

}
