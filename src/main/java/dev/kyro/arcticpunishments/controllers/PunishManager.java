package dev.kyro.arcticpunishments.controllers;

import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticpunishments.ArcticPunishments;
import dev.kyro.arcticpunishments.enums.PunishmentReason;
import dev.kyro.arcticpunishments.enums.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PunishManager implements Listener {

	public static void punish(CommandSender sender, UUID target, PunishmentReason punishmentReason) {
		PunishProfile profile = new PunishProfile(target);
		int bans = profile.getBans();
		int mutes = profile.getMutes();
		boolean hasPerformedMaliciousAction = profile.getMalice();

		PunishmentType punishmentType = punishmentReason.punishmentType;
		if(punishmentReason == PunishmentReason.MALICIOUS_ACTIONS) {
			if(hasPerformedMaliciousAction) {
				punishmentType = PunishmentType.BAN;
			} else {
				profile.setMalice(true);
				mutes = -1;
			}
		}

		switch(punishmentReason) {
			case SCREENSHARE:
			case FROZEN_LOG:
				bans++;
				break;
			case RACISM_DISRESPECT:
				mutes++;
				break;
			case COMPROMISED:
				bans = -1;
		}

		if(punishmentType == PunishmentType.MUTE && mutes == 0) punishmentType = PunishmentType.WARN;
		String punishmentString = punishmentType.getCommand() + " " + profile.getName() + " ";
		if(sender instanceof Player) punishmentString += "--sender=" + sender.getName() + " ";

		switch(punishmentType) {
			case BAN:
				profile.setBans(profile.getBans() + 1);
				if(bans == 0) {
					punishmentString += "14d ";
				} else if(bans == 1 || bans == 2) {
					punishmentString += "30d ";
				} else if(bans > 2) {
					punishmentString += "90d ";
				}
				break;
			case MUTE:
				profile.setMutes(profile.getMutes() + 1);
				if(mutes == -1) {
					punishmentString += "7d ";
				} else if(mutes == 1) {
					punishmentString += "1d ";
				} else if(mutes == 2) {
					punishmentString += "4d ";
				} else if(mutes == 3) {
					punishmentString += "7d ";
				} else if(mutes > 3) {
					punishmentString += "14d ";
				}
				break;
			case WARN:
				profile.setMutes(profile.getMutes() + 1);
		}

		punishmentString += punishmentReason.reason + " -s";
		String finalPunishmentString = punishmentString;
		new BukkitRunnable() {
			@Override
			public void run() {
				System.out.println(finalPunishmentString);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalPunishmentString);
			}
		}.runTask(ArcticPunishments.INSTANCE);
		profile.save();
	}

	public static UUID getUUID(String username) {
		Connection connection = PunishProfile.getConnection();

		try {
			String sql = "SELECT uuid FROM " + PunishProfile.INFO_TABLE + " WHERE username = ?";
			assert connection != null;
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return UUID.fromString(rs.getString("uuid"));
			} else return null;
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}

		try {
			connection.close();
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
		return null;
	}
}
