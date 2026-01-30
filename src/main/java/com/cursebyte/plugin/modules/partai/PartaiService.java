package com.cursebyte.plugin.modules.partai;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartaiService {
    private static final Map<UUID, PartaiData> cache = new HashMap<>();

    /**
     * Ambil data partai; cek cache dulu, jika tidak ada query database.
     */
    public static PartaiData getPartai(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        PartaiData data = PartaiManager.get(uuid);
        if (data != null) {
            cache.put(uuid, data);
        }
        return data;
    }

    /**
     * Ambil partai berdasarkan nama.
     */
    public static PartaiData getPartaiByName(String name) {
        UUID uuid = PartaiManager.getUuidByName(name);
        return uuid != null ? getPartai(uuid) : null;
    }

    /**
     * Update balance partai dan refresh cache.
     */
    public static void updateBalance(UUID uuid, double newBalance) {
        PartaiData data = getPartai(uuid);
        if (data != null) {
            PartaiManager.update(uuid, data.getName(), data.getShortName(), 
                    data.getLeaderUuid(), newBalance, data.getReputation());
            data.setBalance(newBalance);
            cache.put(uuid, data);
        }
    }

    /**
     * Update reputation partai dan refresh cache.
     */
        public static void updateReputation(UUID uuid, double newReputation) {
        PartaiData data = getPartai(uuid);
        if (data != null) {
            double clampedReputation = Math.min(newReputation, 1.0);
            PartaiManager.update(uuid, data.getName(), data.getShortName(), 
                data.getLeaderUuid(), data.getBalance(), clampedReputation);
            data.setReputation(clampedReputation);
            cache.put(uuid, data);
        }
    }

    /**
     * Update leader partai.
     */
    public static void updateLeader(UUID partaiUuid, UUID newLeaderUuid) {
        PartaiData data = getPartai(partaiUuid);
        if (data != null) {
            PartaiManager.update(partaiUuid, data.getName(), data.getShortName(), 
                    newLeaderUuid, data.getBalance(), data.getReputation());
            data.setLeaderUuid(newLeaderUuid);
            cache.put(partaiUuid, data);
        }
    }

    /**
     * Hapus partai dari cache dan database.
     */
    public static void deletePartai(UUID uuid) {
        PartaiManager.delete(uuid);
        cache.remove(uuid);
    }

    /**
     * Clear cache (gunakan saat reload atau shutdown).
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Reload cache dari database.
     */
    public static void reloadCache() {
        cache.clear();
        PartaiManager.getAll().forEach(p -> cache.put(p.getUuid(), p));
    }
}