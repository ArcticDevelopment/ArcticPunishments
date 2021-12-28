package dev.kyro.arcticpunishments.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticpunishments.ArcticPunishments;
import dev.kyro.arcticpunishments.controllers.PunishManager;
import dev.kyro.arcticpunishments.enums.PermissionLevel;
import dev.kyro.arcticpunishments.enums.PunishmentReason;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PunishGUI extends AGUI {
	public UUID targetUUID;
	public OfflinePlayer target;
	public PermissionLevel permissionLevel;

	public PunishPanel punishPanel;
	public ScreensharePanel screensharePanel;

	public PunishGUI(Player player, OfflinePlayer target) {
		super(player);
		this.targetUUID = target.getUniqueId();
		this.target = target;
		this.permissionLevel = PermissionLevel.getPermissionLevel(player);

		this.punishPanel = new PunishPanel(this);
		this.screensharePanel = new ScreensharePanel(this);

		setHomePanel(punishPanel);
	}

	public void punish(PunishmentReason reason) {
		new BukkitRunnable() {
			@Override
			public void run() {
				PunishManager.punish(player, target, reason);
			}
		}.runTaskAsynchronously(ArcticPunishments.INSTANCE);
	}
}
