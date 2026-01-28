package com.cursebyte.plugin.modules.member;

import java.util.UUID;

public class MemberData {
    private UUID playerUuid;
    private String fullName;
    private UUID partaiUuid;
    private String role;

    public MemberData(UUID playerUuid, String fullName, UUID partaiUuid, String role) {
        this.playerUuid = playerUuid;
        this.fullName = fullName;
        this.partaiUuid = partaiUuid;
        this.role = role;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getFullName() {
        return fullName;
    }

    public UUID getPartaiUuid() {
        return partaiUuid;
    }

    public String getRole() {
        return role;
    }
}