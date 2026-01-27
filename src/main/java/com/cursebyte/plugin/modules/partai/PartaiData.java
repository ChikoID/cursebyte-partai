package com.cursebyte.plugin.modules.partai;

import java.util.UUID;

public class PartaiData {
    private UUID uuid;
    private String name;
    private String shortName;
    private UUID leaderUuid;
    private double balance;
    private int reputation;
    private long createdAt;

    public PartaiData(UUID uuid, String name, String shortName, UUID leaderUuid, double balance, int reputation,
            long createdAt) {
        this.uuid = uuid;
        this.name = name;
        this.shortName = shortName;
        this.leaderUuid = leaderUuid;
        this.balance = balance;
        this.reputation = reputation;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public UUID getLeaderUuid() {
        return leaderUuid;
    }

    public double getBalance() {
        return balance;
    }

    public int getReputation() {
        return reputation;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setLeaderUuid(UUID leaderUuid) {
        this.leaderUuid = leaderUuid;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
