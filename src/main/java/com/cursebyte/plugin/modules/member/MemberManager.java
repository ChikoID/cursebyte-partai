package com.cursebyte.plugin.modules.member;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

import com.cursebyte.plugin.database.DatabaseManager;

public class MemberManager {

    public static void initTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS partai_member (
                    player_uuid TEXT PRIMARY KEY,
                    partai_uuid TEXT NOT NULL,
                    jabatan VARCHAR(20) DEFAULT 'ANGGOTA',
                    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (partai_uuid) REFERENCES partai(uuid)
                );
                """;

        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMember(UUID playerUuid, UUID partaiUuid, String jabatan) {
        String sql = "INSERT INTO partai_member (player_uuid, partai_uuid, jabatan) VALUES (?, ?, ?)";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerUuid.toString());
            ps.setString(2, partaiUuid.toString());
            ps.setString(3, jabatan);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRole(UUID playerUuid, String newRole) {
        String sql = "UPDATE partai_member SET jabatan = ? WHERE player_uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setString(2, playerUuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRole(UUID playerUuid) {
        String sql = "SELECT jabatan FROM partai_member WHERE player_uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerUuid.toString());
            var rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("jabatan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UUID getUuidByName(String fullName) {
        String sql = """
                SELECT pm.player_uuid
                FROM partai_member pm
                JOIN citizenship c ON pm.player_uuid = c.uuid
                WHERE c.full_name = ?
                LIMIT 1
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, fullName);
            var rs = ps.executeQuery();

            if (rs.next()) {
                return UUID.fromString(rs.getString("player_uuid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UUID getPartaiUuid(UUID playerUuid) {
        String sql = "SELECT partai_uuid FROM partai_member WHERE player_uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerUuid.toString());
            var rs = ps.executeQuery();

            if (rs.next()) {
                return UUID.fromString(rs.getString("partai_uuid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.util.List<MemberData> getMembersByPartaiUuid(UUID partaiUuid) {
        String sql = "SELECT * FROM partai_member WHERE partai_uuid = ?";
        java.util.List<MemberData> members = new java.util.ArrayList<>();

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, partaiUuid.toString());
            var rs = ps.executeQuery();

            while (rs.next()) {
                MemberData memberData = new MemberData(
                        UUID.fromString(rs.getString("player_uuid")),
                        null, // Nama bisa diisi nanti dari CitizenService
                        partaiUuid,
                        rs.getString("jabatan"));
                members.add(memberData);
                // Lakukan sesuatu dengan data member
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }

    public static void updateRoleInPartai(UUID partaiUuid, UUID memberUuid, String newRole) {
        String sql = "UPDATE partai_member SET jabatan = ? WHERE partai_uuid = ? AND player_uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setString(2, partaiUuid.toString());
            ps.setString(3, memberUuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeMember(UUID playerUuid) {
        String sql = "DELETE FROM partai_member WHERE player_uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerUuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeAllMembers(UUID partaiUuid) {
        String sql = "DELETE FROM partai_member WHERE partai_uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, partaiUuid.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}