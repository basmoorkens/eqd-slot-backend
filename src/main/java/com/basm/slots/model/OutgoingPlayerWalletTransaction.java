package com.basm.slots.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name="outgoing_player_wallet_transaction")
public class OutgoingPlayerWalletTransaction extends PlayerWalletTransaction {


}
