package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class CalledMethodDescription extends AbstractSummaryDescription<CalledMethodDescription> {
	public static final String ID = "calls";
	private final String methodQuery;

	public CalledMethodDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("method")) {
			this.methodQuery = (String) configEntry.get("method");
		} else {
			this.methodQuery = (String) configEntry.get("id");
		}

	}

	public String getMethodQuery() {
		return methodQuery;
	}
}
