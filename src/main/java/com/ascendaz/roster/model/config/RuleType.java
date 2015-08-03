package com.ascendaz.roster.model.config;

public enum RuleType {
	Hard,
	Soft;

	public static RuleType constructType(String string) {
		if (string.equals("Hard")) {
			return RuleType.Hard;
		}
		else if (string.equals("Soft")) {
			return RuleType.Soft;
		}
		return null;
	}
}
