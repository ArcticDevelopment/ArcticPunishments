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

public class PunishManager implements Listener {

	public static void punish(CommandSender sender, OfflinePlayer target, PunishmentReason punishmentReason) {
		APlayer aPlayer = APlayerData.getPlayerData(target);
		FileConfiguration playerData = aPlayer.playerData;
		int bans = playerData.getInt("bans");
		int mutes = playerData.getInt("mutes");
		boolean hasPerformedMaliciousAction = playerData.getBoolean("malice");

		PunishmentType punishmentType = punishmentReason.punishmentType;
		if(punishmentReason == PunishmentReason.MALICIOUS_ACTIONS) {
			if(hasPerformedMaliciousAction) {
				punishmentType = PunishmentType.BAN;
			} else {
				playerData.set("malice", true);
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
		String punishmentString = punishmentType.getCommand() + " " + target.getName() + " ";
		if(sender instanceof Player) punishmentString += "--sender=" + sender.getName() + " ";

		switch(punishmentType) {
			case BAN:
				playerData.set("bans", playerData.getInt("bans") + 1);
				switch(bans) {
					case 0:
						punishmentString += "14d ";
						break;
					case 1:
						punishmentString += "30d ";
						break;
					case 2:
						punishmentString += "90d ";
						break;
					default:
						punishmentString += "";
				}
				break;
			case MUTE:
				playerData.set("mutes", playerData.getInt("mutes") + 1);
				switch(mutes) {
					case -1:
						punishmentString += "7d ";
						break;
					case 1:
						punishmentString += "1h ";
						break;
					case 2:
						punishmentString += "1d ";
						break;
					case 3:
						punishmentString += "7d ";
						break;
					case 4:
						punishmentString += "30d ";
						break;
					default:
						punishmentString += "90d ";
				}
				break;
			case WARN:
				playerData.set("mutes", playerData.getInt("mutes") + 1);
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
		aPlayer.save();
	}
}
