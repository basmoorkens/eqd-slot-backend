package com.basm.slots.repository;

import com.basm.slots.model.IncomingPlayerWalletTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IncomingPlayerWalletTransactionRepository extends CrudRepository<IncomingPlayerWalletTransaction, Long> {

    @Query("SELECT t.pagingToken FROM IncomingPlayerWalletTransaction t where t.id = ( SELECT MAX(x.id) from IncomingPlayerWalletTransaction x)")
    public String findLatestScannedPagingToken();

    /**
     * Finds incoming transactions that should be processed. It filters out duplicates by looking for each TX record if there is an already existing TX record
     * with the same blockchainhas.
     * @param pageable  The pageable
     * @return          List of incoming TX to process.
     */
    @Query("SELECT t FROM IncomingPlayerWalletTransaction t where t.transactionStatus = com.basm.slots.model.TransactionStatus.NEW " +
            "AND NOT EXISTS (" +
                "SELECT t2 FROM IncomingPlayerWalletTransaction t2 where t.blockchainHash = t2.blockchainHash " +
                "AND t2.transactionStatus = com.basm.slots.model.TransactionStatus.DONE" +
            ")")
    public List<IncomingPlayerWalletTransaction> findUnprocessedIncomingTransactions(Pageable pageable);

    @Query("SELECT t FROM IncomingPlayerWalletTransaction t where t.transactionStatus = com.basm.slots.model.TransactionStatus.NEW " +
            "AND EXISTS (" +
            "SELECT t2 FROM IncomingPlayerWalletTransaction t2 where t.blockchainHash = t2.blockchainHash " +
            "AND t2.transactionStatus = com.basm.slots.model.TransactionStatus.DONE" +
            ")")
    public List<IncomingPlayerWalletTransaction> getDuplicateIncomingTransactions(Pageable pageable);

    @Query("SELECT coalesce(SUM(t.amount), 0) FROM IncomingPlayerWalletTransaction t where t.transactionStatus NOT IN (com.basm.slots.model.TransactionStatus.DONE) ")
    public Double findUnprocessedIncomingAmountToPay();
}
