package com.basm.slots.restmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BigPayoutResponse {

    @JsonProperty("message")
    private String messagge;

    public BigPayoutResponse(final String message) {
        this.messagge = message;
    }

    public String getMessagge() {
        return messagge;
    }

    public void setMessagge(String messagge) {
        this.messagge = messagge;
    }
}
