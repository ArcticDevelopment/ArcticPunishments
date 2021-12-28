package dev.kyro.arcticpunishments.enums;

import org.bukkit.entity.Player;

public enum PermissionLevel {
	SCREENSHARE,
	ADMINISTRATOR,
	MODERATOR,
	NONE;

	public static PermissionLevel getPermissionLevel(Player player) {
		if(player.hasPermission("apunish.screenshare")) return SCREENSHARE;
		if(player.hasPermission("apunish.administrator")) return ADMINISTRATOR;
		if(player.hasPermission("apunish.moderator")) return MODERATOR;
		return NONE;
	}
}
