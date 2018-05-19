package com.basm.slots.repository;

import com.basm.slots.model.PlayerWallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PlayerWalletRepository extends CrudRepository<PlayerWallet, Long> {

    public PlayerWallet findByPublicKey(final String publicKey);

}
