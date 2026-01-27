package com.cursebyte.plugin.modules.relasi;

import java.sql.Statement;
import java.util.UUID;

import com.cursebyte.plugin.database.DatabaseManager;

public class RelasiPartaiManager {
    public static void initTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS relasi_partai (
                        partai_uuid TEXT,
                        target_partai_uuid TEXT,
                        relation_type VARCHAR(20),
                        PRIMARY KEY (partai_uuid, target_partai_uuid),
                        FOREIGN KEY (partai_uuid) REFERENCES partai(uuid),
                        FOREIGN KEY (target_partai_uuid) REFERENCES partai(uuid)
                    );
                """;

        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(UUID partaiUuid, UUID targetPartaiUuid, String relationType) {
        String sql = """
                    INSERT OR REPLACE INTO relasi_partai(partai_uuid, target_partai_uuid, relation_type)
                    VALUES(?, ?, ?)
                """;

        try (var ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, partaiUuid.toString());
            ps.setString(2, targetPartaiUuid.toString());
            ps.setString(3, relationType);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRelationType(UUID partaiUuid, UUID targetPartaiUuid) {
        String sql = """
                    SELECT relation_type FROM relasi_partai
                    WHERE partai_uuid = ? AND target_partai_uuid = ?
                """;

        try (var ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, partaiUuid.toString());
            ps.setString(2, targetPartaiUuid.toString());
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("relation_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}