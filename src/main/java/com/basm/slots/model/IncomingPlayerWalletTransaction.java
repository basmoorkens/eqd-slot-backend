package com.basm.slots.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="incoming_player_wallet_transaction")
public class IncomingPlayerWalletTransaction extends PlayerWalletTransaction {

    private String pagingToken;

    public String getPagingToken() {
        return pagingToken;
    }

    public void setPagingToken(String pagingToken) {
        this.pagingToken = pagingToken;
    }
}
