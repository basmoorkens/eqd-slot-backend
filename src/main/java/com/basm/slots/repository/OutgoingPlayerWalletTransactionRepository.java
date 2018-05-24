package com.basm.slots.repository;

import com.basm.slots.model.OutgoingPlayerWalletStellarTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OutgoingPlayerWalletTransactionRepository extends CrudRepository<OutgoingPlayerWalletStellarTransaction, Long> {

    @Query("SELECT t FROM OutgoingPlayerWalletStellarTransaction t where t.transactionStatus = com.basm.slots.model.TransactionStatus.NEW")
    public List<OutgoingPlayerWalletStellarTransaction> findUnprocessedOutgoingTransactions(Pageable pageable);

}
