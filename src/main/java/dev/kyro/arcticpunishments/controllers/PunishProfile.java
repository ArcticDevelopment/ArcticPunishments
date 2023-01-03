package dev.kyro.arcticpunishments.controllers;

import java.sql.*;
import java.util.UUID;

public class PunishProfile {

	public static final String PUNISHMENT_TABLE = "Punishments";
	public static final String INFO_TABLE = "PlayerInfo";


	private final UUID uuid;
	private int bans;
	private int mutes;
	private boolean malice;

	private String name;

	public PunishProfile(UUID uuid) {
		this.uuid = uuid;
		Connection connection = getConnection();
		try {
			String sql = "SELECT bans, mutes, malice FROM " + PUNISHMENT_TABLE + " WHERE uuid = ?";
			assert connection != null;
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, uuid.toString());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				this.bans = rs.getInt("bans");
				this.mutes = rs.getInt("mutes");
				this.malice = rs.getBoolean("malice");
			} else {
				this.bans = 0;
				this.mutes = 0;
				this.malice = false;
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}

		try {
			String sql = "SELECT username FROM " + INFO_TABLE + " WHERE uuid = ?";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, uuid.toString());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				this.name = rs.getString("username");
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}


		try {
			connection.close();
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dbUrl = "***REMOVED***";
			String username = "***REMOVED***";
			String password = "***REMOVED***";
			return DriverManager.getConnection(dbUrl, username, password);
		} catch(Exception ignored) {} ;
		return null;
	}

	public void save() {
		Connection connection = getConnection();
		try {
			String sql = "INSERT INTO " + PUNISHMENT_TABLE + " (uuid, bans, mutes, malice) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE uuid = ?, bans = ?, mutes = ?, malice = ?";
			assert connection != null;
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, uuid.toString());
			stmt.setInt(2, bans);
			stmt.setInt(3, mutes);
			stmt.setBoolean(4, malice);

			stmt.setString(5, uuid.toString());
			stmt.setInt(6, bans);
			stmt.setInt(7, mutes);
			stmt.setBoolean(8, malice);
			stmt.executeUpdate();
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}

		try {
			connection.close();
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}

	}

	public UUID getUuid() {
		return uuid;
	}

	public int getBans() {
		return bans;
	}

	public int getMutes() {
		return mutes;
	}

	public boolean getMalice() {
		return malice;
	}

	public String getName() {
		return name;
	}

	public void setBans(int bans) {
		this.bans = bans;
	}

	public void setMutes(int mutes) {
		this.mutes = mutes;
	}

	public void setMalice(boolean malice) {
		this.malice = malice;
	}

}
