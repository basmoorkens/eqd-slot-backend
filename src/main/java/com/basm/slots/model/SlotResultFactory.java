package com.basm.slots.model;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SlotResultFactory {


    private SlotResult resultX0, resultX1, resultX2, resultX5, resultX10, resultX500;

    public  SlotResultFactory() {
        resultX0 = new SlotResult(0, 1,0);
        resultX1 = new SlotResult(100, 4,1);
        resultX2 = new SlotResult(200, 9,2);
        resultX5 = new SlotResult(500, 25,3);
        resultX10 = new SlotResult(1000, 56,4);
        resultX500 = new SlotResult(50000, 1000,5);
    }

    public List<SlotResult> getSlotResults() {
        return Arrays.asList(resultX0, resultX1, resultX2, resultX5, resultX10, resultX500);
    }

    public List<SlotResult> getSlotResultsReversed() {
        List<SlotResult> slotResults = Arrays.asList(resultX0, resultX1, resultX2, resultX5, resultX10, resultX500);
        Collections.reverse(slotResults);
        return slotResults;
    }

    public SlotResult getResultX0() {
        return resultX0;
    }

    public SlotResult getResultX1() {
        return resultX1;
    }

    public SlotResult getResultX2() {
        return resultX2;
    }

    public SlotResult getResultX5() {
        return resultX5;
    }

    public SlotResult getResultX10() {
        return resultX10;
    }

    public SlotResult getResultX500() {
        return resultX500;
    }
}
