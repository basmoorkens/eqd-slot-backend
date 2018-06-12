package com.basm.slots.repository;

import com.basm.slots.model.PlayerWallet;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PlayerWalletRepository extends CrudRepository<PlayerWallet, Long> {

    public PlayerWallet findByPublicKey(final String publicKey);

    @Query("SELECT coalesce(SUM(w.balance), 0) FROM PlayerWallet w WHERE w.gameWallet = FALSE")
    public Double getAllOpenPlayerWalletBalances();

    @Query("SELECT p FROM PlayerWallet p where p.balance > 0 AND w.gameWallet = FALSE ORDER BY p.balance DESC")
    public List<PlayerWallet> findWalletsWithFunds();
    
}
