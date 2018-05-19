package com.basm.slots.model;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SlotResultFactory {


    private SlotResult resultX01, resultX1, resultX5, resultX10, resultX50;

    public  SlotResultFactory() {
        resultX01 = new SlotResult(10, 1,0);
        resultX1 = new SlotResult(100, 10,1);
        resultX5 = new SlotResult(500, 20,2);
        resultX10 = new SlotResult(1000, 30,3);
        resultX50 = new SlotResult(5000, 100,4);
    }

    public List<SlotResult> getSlotResults() {
        return Arrays.asList(resultX01, resultX1, resultX5, resultX10, resultX50);
    }

    public List<SlotResult> getSlotResultsReversed() {
        List<SlotResult> slotResults = Arrays.asList(resultX01, resultX1, resultX5, resultX10, resultX50);
        Collections.reverse(slotResults);
        return slotResults;
    }

    public SlotResult getResultX01() {
        return resultX01;
    }

    public SlotResult getResultX1() {
        return resultX1;
    }

    public SlotResult getResultX5() {
        return resultX5;
    }

    public SlotResult getResultX10() {
        return resultX10;
    }

    public SlotResult getResultX50() {
        return resultX50;
    }
}
