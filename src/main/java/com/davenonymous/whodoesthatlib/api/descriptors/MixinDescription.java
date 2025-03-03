package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class MixinDescription extends AbstractSummaryDescription<MixinDescription> {
	public static final String ID = "mixins";
	private final String targetClassName;

	public MixinDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("target")) {
			this.targetClassName = (String) configEntry.get("target");
		} else {
			this.targetClassName = (String) configEntry.get("id");
		}
	}

	public String getTargetClassName() {
		return targetClassName;
	}

}
