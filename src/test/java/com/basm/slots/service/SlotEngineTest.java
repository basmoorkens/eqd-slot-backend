package com.basm.slots.service;

import com.basm.slots.model.SlotResult;
import com.basm.slots.model.SlotResultFactory;
import org.junit.Test;
import com.basm.slots.service.SlotService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SlotEngineTest {


    private SlotService slotService = new SlotService();

    @Test
    public void testSlotEngineRandomness() throws IOException {
        SlotResultFactory factory = new SlotResultFactory();
        double startCreditsInWallet = 50000;
        double counter0 = 0, counter1 = 0, counter5 = 0, counter10 = 0, counter50 = 0, counter500 = 0;
        List<Integer> longestWithoutBigWin = new ArrayList<>();
        double loopCounter = 10000;
        int lastBigwin = 0;
        double highest = startCreditsInWallet;
        for (int i = 0; i < loopCounter; i++) {
            SlotResult result = slotService.normalSpin(startCreditsInWallet);
            if (result.getAmount() == factory.getResultX0().getAmount()) {
                counter0++;
            } else if (result.getAmount() == factory.getResultX1().getAmount()) {
                counter1++;
            } else if (result.getAmount() == factory.getResultX2().getAmount()) {
                counter5++;
            } else if (result.getAmount() == factory.getResultX5().getAmount()) {
                counter10++;
            } else if (result.getAmount() == factory.getResultX10().getAmount()) {
                counter50++;
            } else if (result.getAmount() == factory.getResultX500().getAmount()) {
                counter500++;
                longestWithoutBigWin.add(i - lastBigwin);
                lastBigwin = i;
            }
            startCreditsInWallet += 100;
            startCreditsInWallet -= result.getAmount();

            if (startCreditsInWallet > highest) {
                highest = startCreditsInWallet;
            }
            System.out.println("Spin " + i + "New wallet balance: " + startCreditsInWallet);
        }
        double percent0 = (counter0 / loopCounter) * 100d;
        double percent1 = (counter1 / loopCounter) * 100d;
        double percent5 = (counter5 / loopCounter) * 100d;
        double percent10 = (counter10 / loopCounter) * 100d;
        double percent50 = (counter50 / loopCounter) * 100d;
        double percent500 = (counter500 / loopCounter) * 100d;
        System.out.println("Number of 0:" + counter0 + " Percentage: " + percent0);
        System.out.println("Number of 100:" + counter1 + " Percentage: " + percent1);
        System.out.println("Number of 200:" + counter5 + " Percentage: " + percent5);
        System.out.println("Number of 500:" + counter10 + " Percentage: " + percent10);
        System.out.println("Number of 100:" + counter50 + " Percentage: " + percent50);
        System.out.println("Number of 50000:" + counter500 + " Percentage: " + percent500);
        System.out.println("Highest in wallet " + highest);

        int highestNbr= 0;
        int lowest =10000;
        int sum = 0;
        for (Integer l : longestWithoutBigWin) {
            System.out.println("Longest without big win:" + l);
            sum += l;
            if(l < lowest){
                lowest = l;
            }
            if(l > highestNbr) {
                highestNbr = l;
            }
        }
        System.out.println("Average time between big win: " + sum / longestWithoutBigWin.size());
        System.out.println("The max = "+  highestNbr + " the min =" + lowest);
    }

}


