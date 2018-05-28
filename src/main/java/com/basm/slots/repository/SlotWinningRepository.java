package com.basm.slots.repository;


import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.SlotWinning;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SlotWinningRepository extends CrudRepository<SlotWinning, Long> {

    @Query("SELECT w FROM SlotWinning w where w.playerWallet = :playerWallet ORDER by w.id DESC ")
    public List<SlotWinning> findLast10WinningsForWallet(@Param("playerWallet") final PlayerWallet playerWallet, Pageable pageable);
    
    @Query("SELECT count(w) FROM SlotWinning w")
    public int findTotalSpins();

    @Query("SELECT w2 FROM SlotWinning w2 WHERE w2.id = ( SELECT MAX(w.id) FROM SlotWinning w WHERE w.amount = :bigAmount)")
    public SlotWinning findLastBigwin(@Param("bigAmount") final double bigAmount);
    
    @Query("SELECT COUNT(w), w.amount FROM SlotWinning w WHERE w.id > :startId GROUP BY w.amount")
    public Object[] findStatsSinceLastBigSpin(@Param("startId") final long startId);
    
}
