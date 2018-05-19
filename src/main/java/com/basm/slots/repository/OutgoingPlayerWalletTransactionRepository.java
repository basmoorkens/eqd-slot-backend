package com.basm.slots.repository;

import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.model.PlayerWalletTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OutgoingPlayerWalletTransactionRepository extends CrudRepository<OutgoingPlayerWalletTransaction, Long> {

    @Query("SELECT t FROM OutgoingPlayerWalletTransaction t where t.transactionStatus = com.basm.slots.model.TransactionStatus.NEW")
    public List<OutgoingPlayerWalletTransaction> findUnprocessedOutgoingTransactions(Pageable pageable);

    @Query("SELECT coalesce(SUM(t.amount), 0) FROM OutgoingPlayerWalletTransaction t where t.transactionStatus NOT IN (com.basm.slots.model.TransactionStatus.DONE) ")
    public Double findUnprocessedOutgoingTransactionsAmountToPay();
}
