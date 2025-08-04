package Invmalte.cbsys.Util;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLManager {
    private Connection connection;
    public void connect(String host, int port, String database, String username, String password) throws SQLException {
        if (this.connection != null && !this.connection.isClosed())
            return;
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
        this.connection = DriverManager.getConnection(url, username, password);
        createTables();
    }

    public void disconnect() {
        if (this.connection != null)
            try {
                this.connection.close();
            } catch (SQLException sQLException) {}
    }

    public void createTables() throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS booster_data (uuid VARCHAR(36), booster_type VARCHAR(32), amount INT, PRIMARY KEY (uuid, booster_type));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS active_boosters (booster_type VARCHAR(32), end_time BIGINT, PRIMARY KEY (booster_type));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_perks (name VARCHAR(32), maxlevel INT, PRIMARY KEY (name));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_available (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(32), level INT, price INT, PRIMARY KEY (id));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_users (uuid VARCHAR(36), username VARCHAR(32), PRIMARY KEY (uuid));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_strength (uuid VARCHAR(36), level INT, active INT, PRIMARY KEY (uuid));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_haste (uuid VARCHAR(36), level INT, active INT, PRIMARY KEY (uuid));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_fire (uuid VARCHAR(36), level INT, active INT, PRIMARY KEY (uuid));");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS perks_water (uuid VARCHAR(36), level INT, active INT, PRIMARY KEY (uuid));");
    }

    public String loadPerksUser(UUID uuid) {
        String result = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT username FROM perks_users WHERE uuid = ?;");
            ps.setString(1, uuid.toString()   );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void createPerksUser(Player player) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("REPLACE INTO perks_users (uuid, username) VALUES (?, ?);");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            PreparedStatement ps1 = this.connection.prepareStatement("REPLACE INTO perks_strength (uuid, level, active) VALUES (?, 0, 0);");
            ps1.setString(1, player.getUniqueId().toString());
            PreparedStatement ps2 = this.connection.prepareStatement("REPLACE INTO perks_haste (uuid, level, active) VALUES (?, 0, 0);");
            ps2.setString(1, player.getUniqueId().toString());
            PreparedStatement ps3 = this.connection.prepareStatement("REPLACE INTO perks_fire (uuid, level, active) VALUES (?, 0, 0);");
            ps3.setString(1, player.getUniqueId().toString());
            PreparedStatement ps4 = this.connection.prepareStatement("REPLACE INTO perks_water (uuid, level, active) VALUES (?, 0, 0);");
            ps4.setString(1, player.getUniqueId().toString());
            ps.executeUpdate();
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            ps4.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePerksUser(Player player) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("UPDATE perks_users SET username = ? WHERE uuid = ?;");
            ps.setString(1, player.getName());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] loadPerks(UUID uuid) {
        String[] result = new String[8];
        try {
            PreparedStatement ps1 = this.connection.prepareStatement("SELECT level, active FROM perks_strength WHERE uuid = ?;");
            ps1.setString(1, uuid.toString());
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                result[0] = rs1.getString("level");
                result[1] = rs1.getString("active");
            }
            PreparedStatement ps2 = this.connection.prepareStatement("SELECT level, active FROM perks_haste WHERE uuid = ?;");
            ps2.setString(1, uuid.toString());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                result[2] = rs2.getString("level");
                result[3] = rs2.getString("active");
            }
            PreparedStatement ps3 = this.connection.prepareStatement("SELECT level, active FROM perks_fire WHERE uuid = ?;");
            ps3.setString(1, uuid.toString());
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                result[4] = rs3.getString("level");
                result[5] = rs3.getString("active");
            }
            PreparedStatement ps4 = this.connection.prepareStatement("SELECT level, active FROM perks_water WHERE uuid = ?;");
            ps4.setString(1, uuid.toString());
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) {
                result[6] = rs4.getString("level");
                result[7] = rs4.getString("active");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String[] loadSpecificPerks(UUID uuid, String type) {
        String[] result = new String[2];
        try {
            PreparedStatement ps1 = this.connection.prepareStatement("SELECT level, active FROM perks_" + type + " WHERE uuid = ?;");
            ps1.setString(1, uuid.toString());
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                result[0] = rs1.getString("level");
                result[1] = rs1.getString("active");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void updatePerk(Player player, String type, int level, int active) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("UPDATE perks_" + type + " SET level = ?, active = ? WHERE uuid = ?;");
            ps.setInt(1, level);
            ps.setInt(2, active);
            ps.setString(3, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPerkMax(String type) {
        int result = -1;
        try {
            PreparedStatement ps1 = this.connection.prepareStatement("SELECT maxlevel FROM perks_perks WHERE name = ?;");
            ps1.setString(1, type);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                result = rs1.getInt("maxlevel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public int getPerkPrice(String type, int level) {
        int result = -1;
        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT price FROM perks_available WHERE name = ? AND level = ?;");
            ps.setString(1, type);
            ps.setInt(2, level);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setBoosterAmount(UUID uuid, String type, int amount) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("REPLACE INTO booster_data (uuid, booster_type, amount) VALUES (?, ?, ?);");
            ps.setString(1, uuid.toString());
            ps.setString(2, type);
            ps.setInt(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Integer> loadBoosterData(UUID uuid) {
        Map<String, Integer> result = new HashMap<>();
        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT booster_type, amount FROM booster_data WHERE uuid = ?;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                result.put(rs.getString("booster_type"), Integer.valueOf(rs.getInt("amount")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void saveActiveBooster(String booster, long endTime) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("REPLACE INTO active_boosters (booster_type, end_time) VALUES (?, ?);");
            ps.setString(1, booster);
            ps.setLong(2, endTime);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Long> loadActiveBoosters() {
        Map<String, Long> map = new HashMap<>();
        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM active_boosters;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String booster = rs.getString("booster_type");
                long time = rs.getLong("end_time");
                if (System.currentTimeMillis() < time)
                    map.put(booster, Long.valueOf(time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
