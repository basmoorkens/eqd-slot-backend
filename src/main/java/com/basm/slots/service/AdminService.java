package com.basm.slots.service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.controller.AdminController;
import com.basm.slots.model.StatefulConfiguration;
import com.basm.slots.repository.StatefulConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AdminService")
public class AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private StatefulConfigurationRepository statefulConfigurationRepository;

    @Autowired
    private SlotsProperties slotsProperties;

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

}
