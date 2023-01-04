package dev.kyro.arcticpunishments.commands;

import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.arcticapi.misc.ASound;
import dev.kyro.arcticpunishments.controllers.PunishManager;
import dev.kyro.arcticpunishments.controllers.PunishProfile;
import dev.kyro.arcticpunishments.enums.PermissionLevel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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

		UUID uuid;

		try {
			uuid = UUID.fromString(args[0]);
		} catch(Exception e) {
			uuid = PunishManager.getUUID(args[0]);
		}

		if(uuid == null) {
			AOutput.error(player, "Could not find that player");
			return false;
		}

		PunishProfile profile = new PunishProfile(uuid);

		String arg = args[1].toLowerCase();
		if(arg.equals("ban") || arg.equals("bans")) {
			if(profile.getBans() == 0) {
				AOutput.error(player, "That player does not have any bans");
				return false;
			}
			ASound.play(player, Sound.LEVEL_UP, 1, 1);
			profile.setBans(profile.getBans() - 1);
			AOutput.send(player, "Removed a ban from &e" + profile.getName());
		} else if(arg.equals("mute") || arg.equals("mutes")) {
			if(profile.getMutes() == 0) {
				AOutput.error(player, "That player does not have any mutes");
				return false;
			}
			profile.setMutes(profile.getMutes() - 1);
			ASound.play(player, Sound.LEVEL_UP, 1, 1);
			AOutput.send(player, "Removed a mute from &e" + profile.getName());
		} else if(arg.equals("malice")) {
			if(!profile.getMalice()) {
				AOutput.error(player, "That player has not performed a malicious action");
				return false;
			}
			profile.setMalice(false);
			ASound.play(player, Sound.LEVEL_UP, 1, 1);
			AOutput.send(player, "Removed malice from &e" + profile.getName());
		} else {
			AOutput.error(player, "Usage: /unpunish <player/uuid> <ban/mute/malice> (removes 1/resets it NO CONFIRM)");
			return false;
		}
		profile.save();
		return false;
	}
}
