package com.cursebyte.plugin.modules.economy;

import java.util.UUID;

/**
 * Service untuk integrasi dengan sistem ekonomi
 * Placeholder - implementasi sebenarnya tergantung plugin ekonomi yang digunakan
 */
public class EconomyService {
    
    /**
     * Cek saldo pemain
     * 
     * @param playerUuid UUID pemain
     * @return saldo pemain
     */
    public static double getBalance(UUID playerUuid) {
        // Untuk sementara return 0
        return 0.0;
    }

    /**
     * Tambah saldo pemain
     * 
     * @param playerUuid UUID pemain
     * @param amount jumlah yang ditambahkan
     * @return true jika berhasil
     */
    public static boolean deposit(UUID playerUuid, double amount) {
        return true;
    }

    /**
     * Kurangi saldo pemain
     * 
     * @param playerUuid UUID pemain
     * @param amount jumlah yang dikurangi
     * @return true jika berhasil
     */
    public static boolean withdraw(UUID playerUuid, double amount) {
        return has(playerUuid, amount);
    }

    /**
     * Cek apakah pemain punya saldo cukup
     * 
     * @param playerUuid UUID pemain
     * @param amount jumlah yang dicek
     * @return true jika saldo cukup
     */
    public static boolean has(UUID playerUuid, double amount) {
        return getBalance(playerUuid) >= amount;
    }
}
