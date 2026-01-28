package com.cursebyte.plugin.modules.partai;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cursebyte.plugin.database.DatabaseManager;

public class PartaiManager {
    public static void initTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS partai (
                        uuid TEXT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        short_name VARCHAR(20) NOT NULL,
                        leader_uuid TEXT NOT NULL,
                        balance DOUBLE DEFAULT 0.0,
                        reputation INTEGER DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (leader_uuid) REFERENCES citizenship(uuid)
                    );
                """;

        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cek keberadaan partai berdasarkan UUID (alias untuk isPartai).
     */
    public static boolean existsByUuid(UUID uuid) {
        String sql = "SELECT 1 FROM partai WHERE uuid = ? LIMIT 1";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            var rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean existsByName(String name) {
        String sql = "SELECT 1 FROM partai WHERE name = ? LIMIT 1";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean existsByShortName(String shortName) {
        String sql = "SELECT 1 FROM partai WHERE short_name = ? LIMIT 1";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, shortName);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void createIfNotExists(UUID uuid) {
        String sql = "INSERT OR IGNORE INTO partai(uuid) VALUES(?)";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Membuat partai baru dengan data yang diberikan.
     * 
     * @param uuid       UUID partai - Random
     * @param name       Nama partai
     * @param shortName  Singkatan partai
     * @param leaderUuid UUID pemimpin partai
     */

    public static void create(UUID uuid, String name, String shortName, UUID leaderUuid) {
        String sql = """
                    INSERT INTO partai(uuid, name, short_name, leader_uuid, balance, reputation, created_at)
                    VALUES(?, ?, ?, ?, 0.0, 0, CURRENT_TIMESTAMP)
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.setString(3, shortName);
            ps.setString(4, leaderUuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(UUID uuid, String name, String shortName, UUID leaderUuid, double balance,
            int reputation) {
        String sql = """
                    UPDATE partai
                    SET name = ?, short_name = ?, leader_uuid = ?, balance = ?, reputation = ?
                    WHERE uuid = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, shortName);
            ps.setString(3, leaderUuid.toString());
            ps.setDouble(4, balance);
            ps.setInt(5, reputation);
            ps.setString(6, uuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(UUID uuid) {
        String sql = "DELETE FROM partai WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll() {
        String sql = "DELETE FROM partai";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PartaiData get(UUID uuid) {
        String sql = "SELECT * FROM partai WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            var rs = ps.executeQuery();

            if (rs.next()) {
                return new PartaiData(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("name"),
                        rs.getString("short_name"),
                        UUID.fromString(rs.getString("leader_uuid")),
                        rs.getDouble("balance"),
                        rs.getInt("reputation"),
                        rs.getLong("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UUID getUuidByName(String name) {
        String sql = "SELECT uuid FROM partai WHERE name = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            var rs = ps.executeQuery();

            if (rs.next()) {
                return UUID.fromString(rs.getString("uuid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getBalance(UUID uuid) {
        String sql = "SELECT balance FROM partai WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            var rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PartaiData> getAll() {
        String sql = "SELECT * FROM partai";
        List<PartaiData> result = new ArrayList<>();

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql);
                var rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new PartaiData(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("name"),
                        rs.getString("short_name"),
                        UUID.fromString(rs.getString("leader_uuid")),
                        rs.getDouble("balance"),
                        rs.getInt("reputation"),
                        rs.getLong("created_at")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
