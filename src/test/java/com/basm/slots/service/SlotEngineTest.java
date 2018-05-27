package com.basm.slots.service;

import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.SlotResult;
import com.basm.slots.model.SlotResultFactory;
import com.basm.slots.service.PlayerWalletService;
import com.basm.slots.service.StellarService;
import org.junit.Test;
import com.basm.slots.service.SlotService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class SlotEngineTest {


    private SlotService slotService = new SlotService();

    @Test
    public void testSlotEngineRandomness() throws IOException {
       SlotResultFactory factory = new SlotResultFactory();
        double startCreditsInWallet = 500000;
        double counter0=0, counter1=0, counter5=0, counter10=0, counter50=0, counter500=0;
       double loopCounter = 100000;
       double highest = startCreditsInWallet;
       for(int i = 0 ; i < loopCounter ; i++) {
           SlotResult result = slotService.normalSpin(startCreditsInWallet);
           if(result.getAmount() == factory.getResultX01().getAmount()) {
               counter0++;
           } else if (result.getAmount() == factory.getResultX1().getAmount()) {
               counter1++;
           } else if (result.getAmount() == factory.getResultX5().getAmount()) {
               counter5++;
           } else if (result.getAmount() == factory.getResultX10().getAmount()) {
               counter10++;
           } else if (result.getAmount() == factory.getResultX50().getAmount()) {
               counter50++;
           }  else if (result.getAmount() == factory.getResultX500().getAmount()) {
               counter500++;
           }
           startCreditsInWallet+=100;
           startCreditsInWallet-= result.getAmount();
           if(startCreditsInWallet< 50000) {
               System.out.println("Under50K");
           } else {
               System.out.println("Over 50K");
           }
           if(startCreditsInWallet > highest) {
            highest = startCreditsInWallet;
           }
           System.out.println("Spin "+ i + "New wallet balance: " + startCreditsInWallet);
       }
       double percent0 = (counter0/loopCounter)*100d;
        double percent1 = (counter1/loopCounter)*100d;
        double percent5 = (counter5/loopCounter)*100d;
        double percent10 = (counter10/loopCounter)*100d;
        double percent50 = (counter50/loopCounter)*100d;
        double percent500 = (counter500/loopCounter)*100d;
       System.out.println("Number of 0:" + counter0 + " Percentage: " + percent0);
        System.out.println("Number of 100:" + counter1+ " Percentage: " + percent1);
        System.out.println("Number of 500:" + counter5+ " Percentage: " + percent5);
        System.out.println("Number of 1000:" + counter10+ " Percentage: " + percent10);
        System.out.println("Number of 5000:" + counter50+ " Percentage: " + percent50);
        System.out.println("Number of 50000:" + counter500+ " Percentage: " + percent500);
    System.out.println("Highest in wallet " + highest);
    }

}


