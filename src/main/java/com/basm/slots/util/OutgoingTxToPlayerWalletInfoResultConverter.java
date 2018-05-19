package com.basm.slots.util;

import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.restmodel.PlayerWalletInfoResultLine;

public class OutgoingTxToPlayerWalletInfoResultConverter {

    public PlayerWalletInfoResultLine convert(final OutgoingPlayerWalletTransaction outgoingPlayerWalletTransaction) {
        PlayerWalletInfoResultLine result = new PlayerWalletInfoResultLine();
        result.setAmount(outgoingPlayerWalletTransaction.getAmount());
        result.setProcessedTime(outgoingPlayerWalletTransaction.getProcessedDateTime());
        result.setStatus(outgoingPlayerWalletTransaction.getTransactionStatus().toString());
        result.setWonTime(outgoingPlayerWalletTransaction.getCreatedDateTime());
        result.setBlockchainHash(outgoingPlayerWalletTransaction.getBlockchainHash());
        return result;
    }
}
