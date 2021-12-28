package dev.kyro.arcticpunishments.commands;

import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.arcticapi.misc.ASound;
import dev.kyro.arcticpunishments.enums.PermissionLevel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnpunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			AOutput.error(sender, "This command must be used in-game");
			return false;
		}
		Player player = (Player) sender;

		PermissionLevel permissionLevel = PermissionLevel.getPermissionLevel(player);
		if(permissionLevel != PermissionLevel.SCREENSHARE && permissionLevel != PermissionLevel.ADMINISTRATOR) {
			AOutput.error(player, "You do not have permission to do that");
			return false;
		}

		if(args.length < 2) {
			AOutput.error(player, "Usage: /unpunish <player/uuid> <ban/mute/malice> (removes 1/resets it NO CONFIRM)");
			return false;
		}

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
		if(offlinePlayer == null) {
			AOutput.error(player, "Could not find that player");
			return false;
		}

		APlayer aPlayer = APlayerData.getPlayerData(offlinePlayer);
		FileConfiguration playerData = aPlayer.playerData;

		String arg = args[1].toLowerCase();
		if(arg.equals("ban") || arg.equals("bans")) {
			if(playerData.getInt("bans") == 0) {
				AOutput.error(player, "That player does not have any bans");
				return false;
			}
			ASound.play(player, Sound.LEVEL_UP, 1, 1);
			playerData.set("bans", playerData.getInt("bans") - 1);
			AOutput.send(player, "Removed a ban from &e" + offlinePlayer.getName());
		} else if(arg.equals("mute") || arg.equals("mutes")) {
			if(playerData.getInt("mutes") == 0) {
				AOutput.error(player, "That player does not have any mutes");
				return false;
			}
			playerData.set("mutes", playerData.getInt("mutes") - 1);
			ASound.play(player, Sound.LEVEL_UP, 1, 1);
			AOutput.send(player, "Removed a mute from &e" + offlinePlayer.getName());
		} else if(arg.equals("malice")) {
			if(!playerData.getBoolean("malice")) {
				AOutput.error(player, "That player has not performed a malicious action");
				return false;
			}
			playerData.set("malice", false);
			ASound.play(player, Sound.LEVEL_UP, 1, 1);
			AOutput.send(player, "Removed malice from &e" + offlinePlayer.getName());
		} else {
			AOutput.error(player, "Usage: /unpunish <player/uuid> <ban/mute/malice> (removes 1/resets it NO CONFIRM)");
			return false;
		}
		aPlayer.save();
		return false;
	}
}
