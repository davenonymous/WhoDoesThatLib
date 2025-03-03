package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class UsedTypeDescription extends AbstractSummaryDescription<UsedTypeDescription> {
	public static final String ID = "types";
	private final String typeClassName;

	public UsedTypeDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("type")) {
			this.typeClassName = (String) configEntry.get("type");
		} else {
			this.typeClassName = (String) configEntry.get("id");
		}

	}

	public String getTypeClassName() {
		return typeClassName;
	}
}
