package com.basm.slots.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="outgoing_player_wallet_transaction")
public class OutgoingPlayerWalletStellarTransaction extends PlayerWalletStellarTransaction {

	private static final long serialVersionUID = -2813428433019381333L;


}
