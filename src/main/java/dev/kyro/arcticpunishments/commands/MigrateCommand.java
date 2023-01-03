package dev.kyro.arcticpunishments.commands;

import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticpunishments.controllers.PunishProfile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;

public class MigrateCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if(!(commandSender instanceof Player)) return false;
		Player player = (Player) commandSender;

		if(!player.isOp()) return false;

		Connection connection = PunishProfile.getConnection();
		assert connection != null;
		createTable(connection);
		migrate();

		try {
			connection.close();
		} catch(Exception e) { e.printStackTrace(); }


		return false;
	}

	public static void createTable(Connection connection) {
		try {
			Statement stmt = connection.createStatement();
			String createTableSQL = "CREATE TABLE " + PunishProfile.PUNISHMENT_TABLE + " (" +
					"uuid VARCHAR(36) PRIMARY KEY, " +
					"bans INTEGER(255) NOT NULL, " +
					"mutes INTEGER(255) NOT NULL, " +
					"malice BOOLEAN DEFAULT FALSE)";
			stmt.executeUpdate(createTableSQL);
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void migrate() {
		for(Map.Entry<UUID, APlayer> entry : APlayerData.getAllData().entrySet()) {
			UUID uuid = entry.getKey();
			FileConfiguration data = entry.getValue().playerData;

			int bans = data.getInt("bans");
			int mutes = data.getInt("mutes");
			boolean malice = data.getBoolean("malice");

			PunishProfile profile = new PunishProfile(uuid);
			profile.setBans(bans);
			profile.setMutes(mutes);
			profile.setMalice(malice);

			profile.save();
		}
	}
}
