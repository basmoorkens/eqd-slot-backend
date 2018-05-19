package com.basm.slots.repository;

import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.model.PlayerWalletTransaction;
import com.basm.slots.model.TransactionStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutgoingPlayerWalletTransactionRepository extends CrudRepository<OutgoingPlayerWalletTransaction, Long> {

    @Query("SELECT t FROM OutgoingPlayerWalletTransaction t where t.transactionStatus = com.basm.slots.model.TransactionStatus.NEW")
    public List<OutgoingPlayerWalletTransaction> findUnprocessedOutgoingTransactions(Pageable pageable);

    @Query("SELECT coalesce(SUM(t.amount), 0) FROM OutgoingPlayerWalletTransaction t where t.transactionStatus NOT IN (com.basm.slots.model.TransactionStatus.DONE) ")
    public Double findUnprocessedOutgoingTransactionsAmountToPay();

    @Query("SELECT t FROM OutgoingPlayerWalletTransaction t where t.publicKey = :publicKey AND  t.transactionStatus = :status ORDER by t.id DESC ")
    public List<OutgoingPlayerWalletTransaction> findLastOutgoingTransactionsForPublicKey(@Param("publicKey") final String publicKey,@Param("status") final TransactionStatus status, Pageable pageable);
}
