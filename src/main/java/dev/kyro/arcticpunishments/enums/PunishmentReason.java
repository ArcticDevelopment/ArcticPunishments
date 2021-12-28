package dev.kyro.arcticpunishments.enums;

public enum PunishmentReason {

	CHEATING(PunishmentType.BAN, "Blacklisted Modifications"),
	SCREENSHARE(PunishmentType.BAN, "Blacklisted Modifications (Screenshared)"),
	SCREENSHARE_ADMITTED(PunishmentType.BAN, "Blacklisted Modifications (Admitted)"),
	FROZEN_LOG(PunishmentType.BAN, "Logging Out Before/During Screenshare"),
	BOOSTING(PunishmentType.BAN, "Account Boosting"),
	COMPROMISED(PunishmentType.BAN, "Compromised Account"),
	BAN_EVADING(PunishmentType.BAN, "Evading Punishment"),

	SPAMMING(PunishmentType.MUTE, "Spamming"),
	RACISM_DISRESPECT(PunishmentType.MUTE, "Racism/Disrespect"),
	MALICIOUS_ACTIONS(PunishmentType.MUTE, "Malicious Actions Against Player/Server"),
	MUTE_EVADING(PunishmentType.MUTE, "Evading Punishment");

	public PunishmentType punishmentType;
	public String reason;

	PunishmentReason(PunishmentType punishmentType, String reason) {
		this.punishmentType = punishmentType;
		this.reason = reason;
	}

	public static PunishmentReason getPunishmentReason(String reason) {
		for(PunishmentReason value : values()) {
			if(value.reason.equals(reason)) return value;
		}
		return null;
	}
}
