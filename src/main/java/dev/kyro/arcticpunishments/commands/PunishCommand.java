package dev.kyro.arcticpunishments.commands;

import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.arcticpunishments.ArcticPunishments;
import dev.kyro.arcticpunishments.controllers.PunishManager;
import dev.kyro.arcticpunishments.enums.PermissionLevel;
import dev.kyro.arcticpunishments.enums.PunishmentReason;
import dev.kyro.arcticpunishments.inventories.PunishGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			new BukkitRunnable() {
				@Override
				public void run() {
					PunishManager.punish(sender, Bukkit.getOfflinePlayer(args[0]), PunishmentReason.CHEATING);
				}
			}.runTaskAsynchronously(ArcticPunishments.INSTANCE);
			return false;
		}
		Player player = (Player) sender;

		if(PermissionLevel.getPermissionLevel(player) == PermissionLevel.NONE) {
			AOutput.error(player, "You do not have permission to do that");
			return false;
		}

		if(args.length < 1) {
			AOutput.error(player, "Usage: /punish <player/uuid>");
			return false;
		}

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
		if(offlinePlayer == null) {
			AOutput.error(player, "Could not find that player");
			return false;
		}

		if(offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
			AOutput.error(player, "You cannot punish yourself");
			return false;
		}

		new PunishGUI(player, offlinePlayer).open();

		return false;
	}
}
