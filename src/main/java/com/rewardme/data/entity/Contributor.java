package com.rewardme.data.entity;

import javax.persistence.*;

@Entity
public class Contributor {
    @Id
    @GeneratedValue
    private Long id;

    private String uuid;

    private String externalId;

    private long contributed;

    public Contributor() {
    }

    public Contributor(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public long getContributed() {
        return contributed;
    }

    public void setContributed(long contributed) {
        this.contributed = contributed;
    }
}
