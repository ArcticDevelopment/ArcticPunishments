package dev.kyro.arcticpunishments.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticpunishments.ArcticPunishments;
import dev.kyro.arcticpunishments.controllers.PunishManager;
import dev.kyro.arcticpunishments.controllers.PunishProfile;
import dev.kyro.arcticpunishments.enums.PermissionLevel;
import dev.kyro.arcticpunishments.enums.PunishmentReason;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PunishGUI extends AGUI {
	public UUID targetUUID;
	public PermissionLevel permissionLevel;
	public PunishPanel punishPanel;
	public ScreensharePanel screensharePanel;
	public PunishProfile profile;

	public PunishGUI(Player player, UUID target) {
		super(player);
		this.targetUUID = target;
		this.profile = new PunishProfile(targetUUID);

		this.permissionLevel = PermissionLevel.getPermissionLevel(player);
		this.punishPanel = new PunishPanel(this);
		this.screensharePanel = new ScreensharePanel(this);

		setHomePanel(punishPanel);
	}

	public void punish(PunishmentReason reason) {
		new BukkitRunnable() {
			@Override
			public void run() {
				PunishManager.punish(player, targetUUID, reason);
			}
		}.runTaskAsynchronously(ArcticPunishments.INSTANCE);
	}
}
