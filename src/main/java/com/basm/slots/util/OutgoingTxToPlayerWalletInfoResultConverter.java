package com.basm.slots.util;

import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.restmodel.PlayerWalletInfoResultLine;

import java.text.SimpleDateFormat;

public class OutgoingTxToPlayerWalletInfoResultConverter {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PlayerWalletInfoResultLine convert(final OutgoingPlayerWalletTransaction outgoingPlayerWalletTransaction) {
        PlayerWalletInfoResultLine result = new PlayerWalletInfoResultLine();
        result.setAmount(outgoingPlayerWalletTransaction.getAmount());
        result.setStatus(outgoingPlayerWalletTransaction.getTransactionStatus().toString());
        result.setWonTime(sdf.format(outgoingPlayerWalletTransaction.getCreatedDateTime()));
        result.setBlockchainHash(outgoingPlayerWalletTransaction.getBlockchainHash());
        return result;
    }
}
