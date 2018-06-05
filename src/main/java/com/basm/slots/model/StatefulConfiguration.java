package com.basm.slots.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StatefulConfiguration implements Serializable {

	private static final long serialVersionUID = 383036657567535276L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String lastPagingToken;

    private String name;

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
}
