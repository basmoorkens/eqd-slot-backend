package com.basm.slots.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StatefulConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String lastPagingToken;

    private String name;

    private boolean bigPayout;

    public String getLastPagingToken() {
        return lastPagingToken;
    }

    public void setLastPagingToken(String lastPagingToken) {
        this.lastPagingToken = lastPagingToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBigPayout() {
        return bigPayout;
    }

    public void setBigPayout(boolean bigPayout) {
        this.bigPayout = bigPayout;
    }

}
