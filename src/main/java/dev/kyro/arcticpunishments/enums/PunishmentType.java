package dev.kyro.arcticpunishments.enums;

public enum PunishmentType {
	BAN("ipban"),
	MUTE("ipmute"),
	WARN("warn");

	private String command;

	PunishmentType(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}
}
